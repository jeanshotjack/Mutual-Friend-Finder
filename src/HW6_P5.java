import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class HW6_P5 {

    // how many nodes in  adjacency matrix
    public static Integer numNodes;

    // init necessary structures
    public static HashSet<String> hashSet = new HashSet<>();

    public static ArrayList<String> arrayList = new ArrayList<String>();

    // 2D array 
    public static Integer[][] graphAdjMatrix;

    public static TreeMap<String, Integer> inputPosition = new TreeMap<>();

    public static void main(String[] args) {
        String filePath = "src/friends_input.txt";

        //  read the  file
        readFile(filePath);

        //  add elements from hashset to array list
        arrayList.addAll(hashSet);

        // sort array list
        Collections.sort(arrayList);

        // populate input position with elems from arraylist 
        populateInputPosition(arrayList);

        // create adjacency matrix
        setGraphAdjMatrix(filePath, numNodes);

        // print adjacency matrix for test
        System.out.println();
        System.out.println("Adjacency matrix: ");
        System.out.println();
        printGraphAdjMatrix();

        //promt user input
        userInputOptions();
    }
    // method to find mutual friends
    public static ArrayList<String> getMutuals(String node) {
        Integer nodePos = getPositionByNode(node);

        ArrayList<String> friendList = new ArrayList<>();

        for (int i = 0; i < graphAdjMatrix.length; i++) {
            if (graphAdjMatrix[i][nodePos] == 1) {
                friendList.add(getNodeByPosition(i));
            }
        }

        System.out.println("These are friends of " + node + ": " + friendList);
        return friendList;
    }

    //get friends 
    public static ArrayList<String> getFriends(String node) {
        Integer nodePost = getPositionByNode(node);

        ArrayList<String> friendList1 = new ArrayList<>();
        ArrayList<String> friendList2 = new ArrayList<>();
        ArrayList<String> commonFriends = new ArrayList<>();

        for (int i = 0; i < graphAdjMatrix.length; i++) {
            if (graphAdjMatrix[i][nodePost] == 1) {
                friendList1.add(getNodeByPosition(i));
            }
        }
        for (String j : friendList1) {
            Integer nodePosTemp = getPositionByNode(j);
            for (int i = 0; i < graphAdjMatrix.length; i++) {
                if (graphAdjMatrix[i][nodePosTemp] == 1) {
                    friendList2.add(getNodeByPosition(i));
                } 
            }
        }
        for (String k : friendList2) {
            if (!k.equals(node)) {
                commonFriends.add(k);
            }
        }
        for (String z : commonFriends) {
            if (!friendList1.contains(z)) {
                friendList1.add(z);
            }
        }
        return friendList1;
    }

    // checks if they are friends
    public static void areTheyFriends(String friend1, String friend2) {
        Integer nodePos1 = getPositionByNode(friend1);
        Integer nodePos2 = getPositionByNode(friend2);

        ArrayList<String> friendList1 = new ArrayList<>();
        ArrayList<String> friendList2 = new ArrayList<>();

        // populate friend list 1
        for (int i = 0; i < graphAdjMatrix.length; i++) {
            if (graphAdjMatrix[i][nodePos1] == 1) {
                friendList1.add(getNodeByPosition(i));
            }
        }
        // same but for 2
        for (int i = 0; i < graphAdjMatrix.length; i++) {
            if (graphAdjMatrix[i][nodePos2] == 1) {
                friendList2.add(getNodeByPosition(i));
            }
        }

        // if both friend lists contain each others names they're friends
        if (friendList1.contains(friend2) && friendList2.contains(friend1)) {
            System.out.println("They're friends");
        }
        // try to  find a common friend
        else if (!friendList1.contains(friend2) && !friendList2.contains(friend1)) {
            String commonFriend = compareFriendsList(friendList1, friendList2);

            if (!commonFriend.equals("not friends")) {
                ArrayList<String> commonFriendList = getMutuals(commonFriend);

                if (commonFriendList.contains(friend1) && commonFriendList.contains(friend2)) {
                    System.out.println("They're friends");
                }
            }

            else {
                System.out.println("They are not friends");
            }
        }

    }

    // compaer friends of friends
    public static String compareFriendsList(ArrayList<String> friend1, ArrayList<String> friend2) {
        ArrayList<String> friend = new ArrayList();

        // iterate array lists for friend lists
        for (String i : friend1) {
            for (String j : friend2) {
                if (i.equals(j)) {
                    friend.add(i);
                }
            }
        }

        //  return mutual in temp 
        if (friend.size() == 0) {
            return "Not friends";
        }
        else {
            return friend.get(0);
        }
    }
    
    // take filePath and nodes are params and creates  adj matrix
    public static void setGraphAdjMatrix(String filePath, Integer numNodes) {
        graphAdjMatrix = new Integer[numNodes][numNodes];

        // create og matrix of 0s
        for (int i = 0; i < graphAdjMatrix.length; i++) {
            for (int j = 0; j < graphAdjMatrix.length; j++) {
                graphAdjMatrix[i][j] = 0;
            }
        }

        String lineFromFile = "";
        String[] tokens;

        try {
            Scanner fileInput = new Scanner(new File(filePath));

            while (fileInput.hasNextLine()) {
                 //get next line
                lineFromFile = fileInput.nextLine();
                tokens = lineFromFile.split("\\W+");

                // populate
                graphAdjMatrix[getPositionByNode(tokens[0])][getPositionByNode(tokens[1])] = 1;
                graphAdjMatrix[getPositionByNode(tokens[1])][getPositionByNode(tokens[0])] = 1;
            }
        }

        catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage() + " Not found, exiting");
            System.exit(0);
        }
        catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            System.exit(0);
        }
    }

    // print the matrix
    public static void printGraphAdjMatrix() {
        // nested loops for rows and cols
        for (int i = 0; i < graphAdjMatrix.length; i++) {
            for (int j = 0; j < graphAdjMatrix.length; j++) {
                //print
                System.out.print(graphAdjMatrix[i][j]);
                System.out.print(" ");
            }
            System.out.print(" ");
        }
    }

    // return node
    public static String getNodeByPosition(Integer nodePos) {
        // look through keyvalue pairs in tree map
        for (Map.Entry<String, Integer> keyValue : inputPosition.entrySet()) {
            if (keyValue.getValue().equals(nodePos)) {
                return keyValue.getKey();
            }
        }
        return null; // if node aint found
    }

    public static Integer getPositionByNode(String node) {
        return inputPosition.get(node);
    }

    public static void readFile(String filePath) {
        String lineFromFile = "";

        String[] tokens;

        int count = 1;

        try {
            Scanner fileInput = new Scanner(new File(filePath));

            while (fileInput.hasNextLine()) {
                lineFromFile = fileInput.nextLine();

                tokens = lineFromFile.split("\\W+");

                hashSet.add(tokens[0]);
                hashSet.add(tokens[1]);

                count++;
            }
            numNodes = count;
        }

        catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage() + " Can't find the file");
            System.exit(0);
        }

        catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            System.exit(0);
        }
    }



    public static void userInputOptions() {
        boolean bool = true;

        while (bool) {
            //scanner prompt input
            Scanner scan = new Scanner(System.in);

            System.out.println("\nMain Menu\n");
            System.out.println("Search options: ");
            System.out.println("1: Friends of a person");
            System.out.println("2: Friend or not");
            System.out.println("3: Exit");
            System.out.println();
            System.out.println("Enter option number: ");

            // check for mismatch
            try {
                int num = scan.nextInt();
                if (num < 1 || num > 3) {
                    System.out.println("Invalid, please try again");
                    continue;
                }
                else if (num == 1) {
                    userInput1();
                }
                else if (num == 2) {
                    userInput2();
                }
                else if (num == 3) {
                    System.out.println("Exiting.");
                    System.exit(0);
                }

                bool = true;
            }
            // input mismatch exception
            catch(InputMismatchException e) {
                System.err.println(e.getMessage() + " Invalid input, please try again with an int");
            }
        }
    }

    public static void userInput1() {
        boolean bool = true;

        while(bool) {

            Scanner scan  = new Scanner(System.in);

            System.out.println("Enter a name please: ");

            String friendName = scan.next();

            if (arrayList.contains(friendName)) {
                ArrayList<String> friend = getFriends(friendName);
                System.out.println("Friends of " + friendName + " : " + friend);
                break;
            }
            else {
                System.out.println("That's not somebody here. Please type an included name");
            }

            bool = true;
        }

        userInputOptions();
    }

    public static void userInput2() {
        boolean bool = true;

        while (bool) {
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter 2 friends names with space separating: ");

            String friend1 = scan.next();
            String friend2 = scan.next();

            //check if names arent there in the array list
            if (!arrayList.contains(friend1) && !arrayList.contains(friend2)) {
                System.out.println("Sorry, those arent valid names, please try again");
                continue;
            }
            else if (friend1.equals(friend2)) {
                System.out.println("The names are the same. PLease type separate names");
                continue;
            }
            // if they're both there in the list
            else if (arrayList.contains(friend1) && arrayList.contains(friend2)) {
                areTheyFriends(friend1, friend2);
                break;
            }
            //ask to retype names
            else {
                System.out.println("please type valid names please");
            }

            bool = true;
        }
        userInputOptions();
    }

    // populates tree map
    public static void populateInputPosition(ArrayList<String> arrayL) {
        for (int i = 0; i < arrayL.size(); i++) {
            inputPosition.put(arrayL.get(i), i);
        }
    }

}