import com.sun.source.tree.Tree;
import org.pg4200.les05.MyMap;

import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class Ex02 {

    /*
    (SOURCES USED)
    - MyMapBinarySearchTree.java
    */

    /*
    * (COMMENTS)
    * I was a bit unsure on the way you wanted us to create the java file, so I ended up having multiple
    * classes in an Ex02-file instead of what I'd normally do (a .java-file per class).
    * */

    public static class StudentMap<K extends Comparable<K>, V> implements MyMap<K,V>{

        public StudentTree<K, V>[] students = new StudentTree[10];

        public StudentMap() {
            for(int i=0; i<10; i++){
                students[i] = new StudentTree<>();
            }
        }

        //TODO
        public Student getByName(String search_name){
            int i = 0;
            for(StudentTree<K,V> sT : students){
                StudentTree<K,V>.TreeNode node = sT.getByName(search_name);

                if(node != null){
                    System.out.println("YOU FOUND " + search_name.toUpperCase(Locale.ROOT) + " IN TREE NR." + i);
                    return (Student)node.value;
                }
                i++;
            }
            return null;
        }


        @Override
        public void put(K key, V value){
            String k = key.toString();
            students[Integer.parseInt(k.substring(0,1))].put(key, value);
        }

        @Override
        public void delete(K key){
            String k = key.toString();
            students[Integer.parseInt(k.substring(0,1))].delete(key);
        }

        @Override
        public V get(K key){
            String k = key.toString();
            return students[Integer.parseInt(k.substring(0,1))].get(key);
        }


        public StudentTree getStudentTree(Integer index){
            return students[index];
        }

        @Override
        public int size() {
            int sizeTotal = 0;

            for(StudentTree s : students){
                sizeTotal += s.size();
            }
            return sizeTotal;
        }
    }

    public static class StudentTree<K extends Comparable<K>, V>{

        protected class TreeNode {
            public K key;
            public V value;

            public TreeNode left;
            public TreeNode right;
        }
        protected TreeNode root;
        protected int size;



        private void put(K key, V value) {
            Objects.requireNonNull(key);
            root = put(key, value, root);

        }

        private TreeNode put(K key, V value, TreeNode subtree) {

            //checks if the subtree-content is null. If so, it creates the node in the current subtree
            if (subtree == null) {
                TreeNode node = new TreeNode();
                node.key = key;
                node.value = value;
                size++;
                System.out.println("Student has been added to node! size: " + size);
                return node;
            }

            //if the subtree is not null, it compares the current subtree to the node it is on
            int compareInt = key.compareTo(subtree.key);

            //if the int is smaller than 0, it goes to the left branch
            if(compareInt < 0) {
                subtree.left = put(key, value, subtree.left);
                return subtree;
            }

            //if the int is higher than 0, it goes to the right branch
            if(compareInt > 0) {
                subtree.right = put(key, value, subtree.right);
                return subtree;
            }

            //if it hasn't returned by now, it asserts that the comparison is equal, and overwrites the value.
            assert compareInt == 0;
            subtree.value = value;

            return subtree;
        }

        private void delete(K key) {
            Objects.requireNonNull(key);
            root = delete(key, root);
        }

        protected TreeNode delete(K key, TreeNode rootNode) {

            //checks if the rootNode is null
            if(rootNode == null) {
                return null;
            }

            //if not, it compares the subtree-key to the node you're in
            int compareInt = key.compareTo(rootNode.key);

            //if the int is lower, it'll to into the left branch
            if(compareInt < 0) {
                rootNode.left = delete(key, rootNode.left);
                return rootNode;
            }

            //if it's higher it'll go into the right branch
            if(compareInt > 0) {
                rootNode.right = delete(key, rootNode.right);
                return rootNode;
            }

            //if it is equal, we can assume the root you want to delete, is the node you want to delete
            assert compareInt == 0;
            size--;

            //we need to check the amount of children the node has. If it's zero, we just delete the node.

            //if the left node is null, we replace the deleted node with the right branch
            if(rootNode.left == null){
                return rootNode.right;
            }

            //if the right node is null, we replace the deleted node with the left branch
            if(rootNode.right == null){
                return rootNode.left;
            }

            assert rootNode.left != null && rootNode.right != null;

            /*We save the current node to a temp-node. We make the current node into the lowest node
             * from the right branch, then delete the lowest node from the right branch. The left node
             * will be maintained as is from the temp-node.*/
            TreeNode temp = rootNode;
            rootNode = min(temp.right);
            rootNode.right = deleteMin(temp.right);
            rootNode.left = temp.left;

            return rootNode;
        }

        //Finds the lowest node in the branch
        private TreeNode min(TreeNode rootNode){
            if(rootNode.left == null){
                return rootNode;
            }
            return min(rootNode.left);
        }

        //Deletes the lowest node in the branch
        private TreeNode deleteMin(TreeNode rootNode){

            if(rootNode.left == null){
                return rootNode.right;
            }

            rootNode.left = deleteMin(rootNode.left);

            return  rootNode;
        }


        private V get(K key) {
            Objects.requireNonNull(key);
            return get(key, root);
        }

        private V get(K key, TreeNode rootNode) {
            if(rootNode == null){
                return null;
            }

            int compareInt = key.compareTo(rootNode.key);

            //if it is 0, it's the correct node
            if(compareInt == 0){
                return rootNode.value;
            }

            //if it is higher, you need to look in the right side of the tree
            else if (compareInt > 0) {
                return get(key, rootNode.right);
            }

            //if it is lower, you need to look in the left side of the tree
            else if (compareInt < 0) {
                return get(key, rootNode.left);
            }

            return null;
        }

        //TODO getByNameMethod
        private TreeNode getByName(String name){
            Objects.requireNonNull(name);
            return getByName(name, root);
        }

        private TreeNode getByName(String name, TreeNode rootNode){
            if(name == null){
                return null;
            }

            if(rootNode == null){
                return null;
            }

            assert rootNode.value instanceof Student;

            if(((Student) rootNode.value).firstName.equals(name)){
                return rootNode;
            }

            if (rootNode.left != null) {
                rootNode.left = getByName(name, rootNode.left);
            }

            if (rootNode.right != null) {
                rootNode.right = getByName(name, rootNode.right);
            }

            return null;
        }

        public int getMaxTreeDepth() {
            if(root == null){
                return 0;
            }

            return depth(root);
        }

        protected int depth(TreeNode node){
            int leftDepth = 0;
            int rightDepth = 0;

            if(node.left != null) {
                leftDepth = depth(node.left);
            }

            if(node.right != null) {
                rightDepth = depth(node.right);
            }

            return 1 + Math.max(leftDepth, rightDepth);
        }

        private int size() {
            return size;
        }
    }

    public static class Student{
        String firstName;
        String lastName;
        String id;

        public Student(String firstName, String lastName, String id) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}



