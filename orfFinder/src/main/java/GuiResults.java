import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.sql.*;

public class GuiResults extends JFrame  implements ComponentListener, ActionListener {

    private String seq;
    private Map<Integer, ORF> orfs;
    private Container window;
    JPanel upperpanel;
    JTable results;

    public GuiResults(String inputSeq) {
        this.seq = inputSeq;
        setSize(700, 700);
        createloading();
        setVisible(true);
    }

    private void createloading() {
        window = getContentPane();
        JLabel results = new JLabel("loading results.....");
        window.add(results);
    }

    public void createGui() {
        window.removeAll();
        revalidate();
        repaint();
        window.setLayout(new FlowLayout());
        upperpanel = new JPanel();
        upperpanel.setLayout(new GridLayout());
        upperpanel.setPreferredSize(new Dimension(window.getWidth(), window.getHeight()-50));
        makeTablObj();

        JPanel lowerpanel = new JPanel();

        JLabel info = new JLabel("click to select orf (ctrl+ click to select multiple at once)");
        JButton blast = new JButton("blast selected orfs");
        blast.setActionCommand("blast");
        blast.addActionListener(this);
        JButton save = new JButton("save selected to db");
        save.setActionCommand("save");
        save.addActionListener(this);

        lowerpanel.add(info);
        lowerpanel.add(blast);
        lowerpanel.add(save);

        window.addComponentListener(this);

        window.add(upperpanel);
        window.add(lowerpanel);

    }

    public void callORFFinder() {
        ORFFinder orfFinder = new ORFFinder(seq);
        orfs = orfFinder.getORfs();
    }

    public void makeTablObj() {
        Object[][] data = new Object[orfs.size()][5];
        System.out.println(orfs.size());
        try {
            for(int i = 0; i< orfs.size(); i++){
                data[i][0] = orfs.get(i+1).getORFNumber();
                data[i][1] = orfs.get(i+1).getStartPosition();
                data[i][2] = orfs.get(i+1).getLength();
                data[i][3] = orfs.get(i+1).getDirection();
                data[i][4] = orfs.get(i+1).getSequence();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        String[] columNames = {"ORF", "start","length", "direction", "sequence"};

        results = new JTable(data, columNames);
        results.setFillsViewportHeight(true);
        JScrollPane scrollpane = new JScrollPane(results);
        upperpanel.add(scrollpane);
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

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        int[] selection = results.getSelectedRows();
        if(actionEvent.getActionCommand().equals("blast")){
            System.out.println("work in progress");
        }
        if(actionEvent.getActionCommand().equals("save")){

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com:3306/owe7_pg2", "owe7_pg2@hannl-hlo-bioinformatica-mysqlsrv",
                        "blaat1234");

                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT MAX(ORFID) FROM orf");
                rs.next();
                int orfID = rs.getInt("MAX(ORFID)");
                ResultSet rs2 = st.executeQuery("SELECT MAX(seqID) FROM input");
                rs2.next();
                int seqID= rs2.getInt("MAX(seqID)")+1;
                String updateQuery = "insert into Input (SeqID, Sequence) VALUES ("+seqID+", '"+seq+"')";
                System.out.println(updateQuery);
                st.executeUpdate(updateQuery);

                for (Integer selected : selection) {
                    System.out.println(selected);
                    System.out.println(orfs.get(selected + 1).getStartPosition());
                    orfID = orfID +1;
                    st.executeUpdate("insert into ORF (ORFID, StartPos, ORFLength, ORFSeq, Input_SeqID) VALUES ("+ orfID+", "+ orfs.get(selected + 1).getStartPosition()+", "+orfs.get(selected + 1).getLength()+",'"+orfs.get(selected + 1).getSequence() +"',"+seqID+")");
                }
                conn.close();

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }



        }
    }
}
