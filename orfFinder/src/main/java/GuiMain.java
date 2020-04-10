import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.List;

public class GuiMain extends JFrame implements ActionListener, ComponentListener {

    String userFile = "";
    JTextField filePathTF;
    JTextArea sequentieTA;
    JLabel sequentieLabel;
    JPanel upperpanel;
    Container window;
    static GuiMain frame;

    public static void main(String[] args) {
        frame = new GuiMain();
        frame.setSize(700, 500);
        frame.createMainGui();
        frame.setVisible(true);

    }
    private void createMainGui(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        window = getContentPane();
        window.setLayout(new FlowLayout());

//      panel and elements for inputing a file
        JPanel browsPanel = new JPanel();

        JLabel fileLocation = new JLabel("bestands locatie:");
        filePathTF = new JTextField();
        filePathTF.setPreferredSize(new Dimension(300, 20));
        JButton browseButton = new JButton("browse");
        browseButton.setActionCommand("browse");
        browseButton.addActionListener(this);

        window.add(browsPanel);
        browsPanel.add(fileLocation);
        browsPanel.add(filePathTF);
        browsPanel.add(browseButton);

//        panel and elements for manualy inputting sequentie

        JPanel sequentiePanel = new JPanel();
        sequentiePanel.setLayout(new BoxLayout(sequentiePanel, BoxLayout.Y_AXIS));

        sequentieLabel = new JLabel("voer hier je sequentie in:");
        sequentieTA = new JTextArea();
        sequentieTA.setLineWrap(true);
        sequentieTA.setWrapStyleWord(true);
        JScrollPane scrollpane = new JScrollPane(sequentieTA);
        scrollpane.setHorizontalScrollBar(null);

        scrollpane.setPreferredSize(new Dimension(500, 300));

        window.add(sequentiePanel);
        sequentiePanel.add(sequentieLabel);
        sequentiePanel.add(scrollpane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
//        buttonPanel.setPreferredSize(new Dimension(300, 40));
        JButton find = new JButton("find");
        find.setActionCommand("find");
        find.addActionListener(this);

        JButton DBPage = new JButton("go to DB page");
        DBPage.setActionCommand("DBPage");
        DBPage.addActionListener(this);

        window.add(buttonPanel);
        buttonPanel.add(find);
        buttonPanel.add(DBPage);

    }

    private void createDBGui(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        window = getContentPane();
        window.setLayout(new FlowLayout());
        window.addComponentListener(this);
        upperpanel = new JPanel();
        upperpanel.setLayout(new GridLayout());
        upperpanel.setPreferredSize(new Dimension(window.getWidth(), window.getHeight()-50));
        window.add(upperpanel);
        createResultsTable(new String[] {"ORFID", "StartPos", "ORFlength", "ORFSeq"}, "SELECT * FROM ORF");

        JPanel lowerpanel = new JPanel();
        JButton goback = new JButton("back");
        goback.setActionCommand("goBack");
        goback.addActionListener(this);

        JButton showBlast = new JButton("show blast");
        showBlast.addActionListener(this);
        showBlast.setActionCommand("showBlast");

        JButton showDB = new JButton("show database");
        showDB.setActionCommand("showDB");
        showDB.addActionListener(this);

        JButton showSeq = new JButton("show input seq");
        showSeq.setActionCommand("showSeq");
        showSeq.addActionListener(this);

        lowerpanel.add(goback);
        lowerpanel.add(showBlast);
        lowerpanel.add(showDB);
        lowerpanel.add(showSeq);
        window.add(lowerpanel);

    }
    private void createResultsTable(String[] columnames, String query){
        DefaultTableModel model = new DefaultTableModel();
        JTable results = new JTable(model);

        for(String name: columnames){
            model.addColumn(name);
        }
        try
        {
            upperpanel.removeAll();
            upperpanel.revalidate();
            upperpanel.repaint();
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com:3306/owe7_pg2", "owe7_pg2@hannl-hlo-bioinformatica-mysqlsrv",
                    "blaat1234");

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);



            while (rs.next()) {
                Object[] colum = new Object[columnames.length];

                for(int i =0; i<columnames.length; i++){
                    colum[i] = rs.getObject(columnames[i]);
                }
                model.addRow(colum);
            }
            st.close();

            Vector data = model.getDataVector();
            Vector row = (Vector) data.elementAt(0);

            int mColIndex = 0;
            List colData = new ArrayList(results.getRowCount());
            for (int i = 0; i < results.getRowCount(); i++) {
                row = (Vector) data.elementAt(i);
                colData.add(row.get(mColIndex));
            }

        }
        catch (Exception e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e);
            e.printStackTrace();
        }
        upperpanel.add(new JScrollPane(results));
    }


    private String filePicker() {
        FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String file = dialog.getFile();
        return ((dialog.getDirectory()) + file);
    }

    private void fileToTextarea(){
        String filePath = filePathTF.getText();
        try{

            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = null;
            while ((line = br.readLine()) != null)
            {
                if(!line.startsWith(">"))
                sequentieTA.append(line);
            }

        }catch (Exception e){e.printStackTrace();}



    }


    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "browse":
                userFile = filePicker();
                filePathTF.setText(userFile);
                fileToTextarea();

                break;
            case "DBPage":
                frame.getContentPane().removeAll();
                frame.revalidate();
                frame.repaint();
                frame.createDBGui();
                frame.setVisible(true);
                break;
            case "find":
                    String seq = sequentieTA.getText().toUpperCase();
                    if (seq.equals("") || seq.matches(".*B.*|.*J.*|.*O.*|.*X.*|.*Z.*")) {
                        sequentieLabel.setText("Voer een fatsoenlijke eiwit seqeuntie in!");
                        System.out.println("voer een fatsoenlijke sequentie in man");
                    } else {

                        GuiResults results = new GuiResults(seq);
                        results.callORFFinder();
                        results.createGui();

                }
                break;
            case "goBack":
                frame.getContentPane().removeAll();
                frame.revalidate();
                frame.repaint();
                frame.createMainGui();
                frame.setVisible(true);
                break;
            case "showBlast":
                createResultsTable(new String[] {"BLASTResID", "Sequence", "EValue", "BITScore", "Coverage"}, "SELECT * FROM BLASTResults");
                break;
            case "showDB":
                createResultsTable(new String[] {"ORFID", "StartPos", "ORFlength", "ORFSeq", "Input_SeqID"}, "SELECT * FROM ORF");
                break;
            case "showSeq":
                createResultsTable(new String[] {"SeqID", "Sequence"},"SELECT * FROM input");
                break;


        }

    }



    @Override
    public void componentResized(ComponentEvent componentEvent) {
        if(componentEvent.getSource().equals(window)){
            upperpanel.setPreferredSize(new Dimension(componentEvent.getComponent().getWidth(), componentEvent.getComponent().getHeight()-50 ));

        }
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {

    }

    @Override
    public void componentHidden(ComponentEvent componentEvent) {

    }
}
