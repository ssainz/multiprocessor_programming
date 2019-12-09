package edu.vt.ece.searchtree.redblacktree.gui;


import edu.vt.ece.searchtree.redblacktree.SearchTree;
import edu.vt.ece.searchtree.redblacktree.SearchTreeNode;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * https://github.com/EslaMx7/AI-Tasks-JADE-Tests/blob/master/src/trees/tasks/treeGUI.java
 */



class DrawTree<Key extends Comparable<Key>,Value> extends JPanel{

    public SearchTree<Key,Value> tree;

    public DrawTree(SearchTree<Key,Value> tree){
        this.tree = tree;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // TODO Auto-generated method stub

        g.setFont(new Font("Tahoma", Font.BOLD, 8));

        //g.drawString(String.valueOf(tree.root.data), this.getWidth()/2, 30);


        //DrawNode(g, tree.root,100, 50,2);

        drawTree(g, 0, getWidth(), 0, getHeight() / (tree.maxDepth() + 2), tree.getRootV2());
    }

    public void DrawNode(Graphics g, SearchTreeNode n, int w, int h, int q){
        g.setFont(new Font("Tahoma", Font.BOLD, 8));

        if(n!=null){
            String strColor = "";
            if(n.getColor()){
                strColor = "R";
                g.setColor(Color.red);
            }else{
                strColor = "B";
                g.setColor(Color.black);
            }
            g.drawString(String.valueOf(n.getValue()) + strColor, (this.getWidth()/q)+w, h);

            if(n.getLeft() !=null)
                DrawNode(g, n.getLeft(), -w, h*2, q);
            //DrawNode(g, n.left, -w, h*2, q);
            //g.drawString(String.valueOf(n.left.data), (this.getWidth()/q)-w, h+50);
            if(n.getRight() !=null)
                DrawNode(g, n.getRight(), w*2, h*2, q);
            //g.drawString(String.valueOf(n.right.data), (this.getWidth()/q)+w, h+50);
        }




    }


    public void drawTree(Graphics g, int StartWidth, int EndWidth, int StartHeight, int Level, SearchTreeNode node) {
        String data = "NIL";
        if(node != null){
            data = String.valueOf(node.getValue());
        }

        g.setFont(new Font("Tahoma", Font.BOLD, 5));
        FontMetrics fm = g.getFontMetrics();
        int dataWidth = fm.stringWidth(data);

        if(node != null && node.getColor()){
            g.setColor(Color.red);
        }else{
            g.setColor(Color.black);
        }
        g.drawString(data, (StartWidth + EndWidth) / 2 - dataWidth / 2, StartHeight + Level / 2);

        if (node != null && node.getLeft() != null)
            drawTree(g, StartWidth, (StartWidth + EndWidth) / 2, StartHeight + Level, Level, node.getLeft());

        if (node != null && node.getRight() != null)
            drawTree(g, (StartWidth + EndWidth) / 2, EndWidth, StartHeight + Level, Level, node.getRight());
    }


}