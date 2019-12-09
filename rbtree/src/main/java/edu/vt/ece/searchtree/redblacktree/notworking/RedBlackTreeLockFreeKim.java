package edu.vt.ece.searchtree.redblacktree.notworking;

import edu.vt.ece.searchtree.redblacktree.SearchTree;
import edu.vt.ece.searchtree.redblacktree.SearchTreeNode;

public class RedBlackTreeLockFreeKim <Key extends Comparable<Key>, Value> implements SearchTree<Key, Value> {
    RedBlackTreeNodeLockFreeKim z = null;
    RedBlackTreeNodeLockFreeKim root = null;

    public RedBlackTreeLockFreeKim() {
        this.z = new RedBlackTreeNodeLockFreeKim(); // parent of root
        this.root = null;
    }

    @Override
    public boolean putV2(Key key, Value val) {

        RedBlackTreeNodeLockFreeKim x = new RedBlackTreeNodeLockFreeKim(key, val);
        restart:while(true){
            RedBlackTreeNodeLockFreeKim z = this.z;
            while(!z.flag.compareAndSet(false, true));

            RedBlackTreeNodeLockFreeKim y = this.root;

            while(y != null){
                z = y;
                if(x.key.compareTo(y.key)  < 0) y = y.left;
                else y = y.right;
                if(!y.flag.compareAndSet(false,true)){
                    z.flag.set(false);
                    continue restart;
                }
                if(y != null)
                    z.flag.set(false);
            } /* end while */

            x.flag.set(true);

            if(!SetupLocalAreaForInsert(z)){
                z.flag.set(false);
                continue restart;
            }
            /* Place new node x as child of z */

            x.parent = z;
            if(z == this.z){ this.root = x; z.left = x;}
            else if( x.key.compareTo(z.key) < 0 ) z.left = x;
            else z.right = x;
            x.isRed = true;

            RB_Insert_Fixup(x);

            return true;
        }


    }

    private void RB_Insert_Fixup(RedBlackTreeNodeLockFreeKim x) {
    }

    private boolean SetupLocalAreaForInsert(RedBlackTreeNodeLockFreeKim z) {
        // Try to get flags for rest of local area
        RedBlackTreeNodeLockFreeKim uncle = null;
        RedBlackTreeNodeLockFreeKim zp = z.parent; // take a copy of our parent pointer
        if(!zp.flag.compareAndSet(false, true))
            return false;
        if (zp != z.parent){ // parent has changed - abort
            zp.flag.set(false);
            return false;
        }
        if(z == z.parent.left) // uncle is the right child
            uncle = z.parent.right;
        else  //uncle is the left child
            uncle = z.parent.left;
        if(!uncle.flag.compareAndSet(false, true)){
            z.parent.flag.set(false); // Cannot get uncle's flag
            return false;
        }
        // Now try to get hte four intention markers above z.parent
        // the second argument is only useful for deletes so we pass z
        // which is not an ancestor of z.parent and will have no effect
        if(!GetFlagsAndMarkersAbove(z.parent,z)){
            z.parent.flag.set(false);
            uncle.flag.set(false);
            return false;
        }
        return true;
    }

    private boolean GetFlagsAndMarkersAbove(RedBlackTreeNodeLockFreeKim start, RedBlackTreeNodeLockFreeKim numAdditional) {
        // Check for a moveUpStruct provided by another process (due to Move-Up rule processing)
        // and set 'PIDtoIgnore' to the PID provided in that structure.
        // Use the 'IsIn' function to determine if a node is in the moveUpStruct.
        // Start by getting flags on the four ndoes we have markers on
        //if(!GetFlagsForMarkers())

        // Here paper does not define how to implement the method: ApplyMoveUpRule...
        return false;
    }

    @Override
    public Value get(Key key) {
        return null;
    }

    @Override
    public boolean delete(Key key) {
        return false;
    }

    @Override
    public void end() {

    }

    @Override
    public int maxDepth() {
        return 0;
    }

    @Override
    public SearchTreeNode<Key, Value> getRootV2() {
        return null;
    }

}
