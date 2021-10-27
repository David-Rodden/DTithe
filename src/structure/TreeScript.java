package structure;

public class TreeScript extends ScriptRecord {
    private static final int KILL_PROCESS = -1;
    private TreeTask head;
    private String taskDescription;

    /**
     * Work through the entire tree yourself, then apply the head to the Taskhandler head
     *
     * @param head
     */
    public void setHead(final TreeTask head) {
        this.head = head;
    }

    public boolean hasHeadBeenSet() {
        return head != null;
    }

    protected String getTaskDescription() {
        return taskDescription;
    }

    public int traverseTree() {
//        System.out.println("Starting at head");
        TreeTask current = head;
        while (current != null && !current.isLeaf()) {
            current = current.validate() ? current.getRight() : current.getLeft();
//            System.out.println("current node: " + current);
        }
//        System.out.println("Ended at node: " + current);
        if (current == null) return KILL_PROCESS;
        taskDescription = current.toString();
        return current.execute();
    }

    // Clean up any scheduled threads in any of the nodes
    protected void destructTree() {
        destructTree(head);
    }

    private void destructTree(final TreeTask currentNode) {
        if (currentNode == null) return;
        currentNode.deconstruct();
        TreeTask child;
        if ((child = currentNode.getLeft()) != null) destructTree(child);
        if ((child = currentNode.getRight()) != null) destructTree(child);
    }
}
