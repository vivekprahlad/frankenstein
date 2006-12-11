package com.thoughtworks.frankenstein.common;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * Decodes renderers
 * @author vivek
 */
public interface RendererDecoder {
    public String decodeComboBox(ListCellRenderer renderer, JComboBox comboBox, int index);
    public String decodeList(ListCellRenderer renderer, JList list, int index);
    public String decodeTableCell(TableCellRenderer renderer, JTable table, int row, int column);
    public String decodeTreeCell(TreeCellRenderer renderer, JTree tree, int row, int column);
}
