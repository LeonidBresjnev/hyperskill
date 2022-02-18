package viewer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SQLiteViewer extends JFrame {
    JTextField FileNameTextField;
    JButton OpenFileButton;
    JComboBox TablesComboBox;
    JTextArea QueryTextArea;
    JButton ExecuteQueryButton;
    DefaultTableModel model;

    JTable table ;

    public SQLiteViewer() {
        super("SQLite Viewer");
        model = new DefaultTableModel();
        table = new JTable(model);
        table.setName("Table");
        table.setBounds(50, 500, 600, 350);
        table.setEnabled(true);
        add(table);



        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 900);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        FileNameTextField = new JTextField();
        FileNameTextField.setBounds(50,20, 400,30);
        FileNameTextField.setName("FileNameTextField");
        this.add(FileNameTextField);
        FileNameTextField.setEnabled(true);

        OpenFileButton = new JButton("Open");
        OpenFileButton.setName("OpenFileButton");
        OpenFileButton.setBounds(500, 20, 150, 30);
        add(OpenFileButton);
        OpenFileButton.setEnabled(true);

        TablesComboBox = new JComboBox();
        TablesComboBox.setName("TablesComboBox");
        TablesComboBox.setEnabled(true);
        TablesComboBox.setBounds(50,80, 600,30);
        add(TablesComboBox);

        QueryTextArea = new JTextArea();
        QueryTextArea.setName("QueryTextArea");
        QueryTextArea.setEnabled(false);
        QueryTextArea.setBounds(50,140, 400,300);
        add(QueryTextArea);

        ExecuteQueryButton = new JButton("Execute");
        ExecuteQueryButton.setName("ExecuteQueryButton");
        ExecuteQueryButton.setBounds(500, 140, 150, 30);
        add(ExecuteQueryButton);
        ExecuteQueryButton.setEnabled(false);

        ExecuteQueryButton.addActionListener(e -> {
dataProvider myDataProvider = new dataProvider(FileNameTextField.getText());
table dataset = myDataProvider.getData(QueryTextArea.getText());
model.setColumnIdentifiers(dataset.colnames);
for (Object[] row : dataset.data) {
model.addRow(row);
}
        });

        OpenFileButton.addActionListener(e -> {
            dataProvider myDataProvider = new dataProvider(FileNameTextField.getText());
            List<String> tables = null;
            TablesComboBox.setEnabled(false);
            QueryTextArea.setEnabled(false);
            ExecuteQueryButton.setEnabled(false);
            tables = myDataProvider.getTables();

            TablesComboBox.removeAllItems();
            if (tables.size() == 0) {
                JOptionPane.showMessageDialog(new Frame(), "File doesn't exist");
                ExecuteQueryButton.setEnabled(false);
            } else {
                for (String table : tables) {
                    TablesComboBox.addItem(table);
                    System.out.println(table);
                }

                TablesComboBox.updateUI();
                TablesComboBox.setEnabled(true);
                QueryTextArea.setEnabled(true);
                ExecuteQueryButton.setEnabled(true);
            }
            if (TablesComboBox.getItemCount() > 0) {
                System.out.println("selected: " + TablesComboBox.getSelectedItem());
                QueryTextArea.setText("SELECT * FROM " + TablesComboBox.getSelectedItem() + ";");
            }
        });

        TablesComboBox.addActionListener(e -> {
            System.out.println("selected: " + TablesComboBox.getSelectedItem());
            if (TablesComboBox.getItemCount() > 0) {
                QueryTextArea.setText("SELECT * FROM " + TablesComboBox.getSelectedItem() + ";");
            }
        });
    }
}
