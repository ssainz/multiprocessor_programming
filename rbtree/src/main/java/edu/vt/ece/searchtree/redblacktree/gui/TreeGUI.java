package edu.vt.ece.searchtree.redblacktree.gui;

import edu.vt.ece.searchtree.redblacktree.RedBlackTreeNonThreadSafe;
import edu.vt.ece.searchtree.redblacktree.SearchTree;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TreeGUI<Key extends Comparable<Key>,Value> extends JFrame {

    private JPanel contentPane;
    public SearchTree<Key,Value> tree;
    public DrawTree drawer;

    /**
     * Create the frame.
     */
    public TreeGUI(SearchTree<Key,Value> tree, String title) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));


        //Border border = BorderFactory.createTitledBorder(title);
        //contentPane.setBorder(border);
        setTitle(title);

        drawer = new DrawTree(tree);

        contentPane.add(drawer);
        setContentPane(contentPane);
        this.tree = tree;
        setVisible(true);

    }

}