import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;
public class Client  implements ActionListener{

    JTextField text;
    static JPanel a1;
    
    static Box vertical = Box.createVerticalBox();
    static DataOutputStream dout;
    static JFrame f = new JFrame();
    Client(){
        f.setLayout(null);
        //Header Panel
        JPanel p1 = new JPanel();
        p1.setBackground(Color.BLUE);
        p1.setBounds(0,0,450,70);//from top left corner i.e. origin with frame size lengthwise and height 70
        p1.setLayout(null);
        f.add(p1);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("back.png"));
        Image i2 = i1.getImage().getScaledInstance(25,25,Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel back = new JLabel(i3);
        back.setBounds(5,20,25,25); //5 from left and img size is 25,25
        p1.add(back);

        back.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent ae){
                System.exit(0); //setVisible(false) can also be used
            }
        });

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("profile2.jpg"));
        Image i5 = i4.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel profile = new JLabel(i6);
        profile.setBounds(40,10,50,50);
        p1.add(profile);

        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("video.webp"));
        Image i8 = i7.getImage().getScaledInstance(40,40,Image.SCALE_DEFAULT);
        ImageIcon i9 = new ImageIcon(i8);
        JLabel video = new JLabel(i9);
        video.setBounds(300,15,40,40);
        p1.add(video);

        ImageIcon i10 = new ImageIcon(ClassLoader.getSystemResource("phone.png"));
        Image i11 = i10.getImage().getScaledInstance(40,40,Image.SCALE_DEFAULT);
        ImageIcon i12 = new ImageIcon(i11);
        JLabel phone = new JLabel(i12);
        phone.setBounds(360,15,40,40);
        p1.add(phone);

        JLabel name = new JLabel("Anushka");
        name.setBounds(110,15,100,18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("BELL MT",Font.BOLD,20));
        p1.add(name);

        JLabel status = new JLabel("Active Now");
        status.setBounds(110,35,100,18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("Bell MT",Font.ITALIC,20));
        p1.add(status);

        //Text Area Panel
        a1 = new JPanel();
        a1.setBounds(5,75,440,570); //left-5,up-75 (since header 70),width-440,height-570
        f.add(a1); //a1 added to frame

        text = new JTextField();
        text.setBounds(5,655,310,40); //570+75 space taken above already hence 655
        text.setFont(new Font("Sans Serif",Font.PLAIN,16));
        f.add(text);

        JButton send = new JButton("Send");
        send.setBounds(320,655,123,40);
        send.setBackground(Color.BLUE);
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        send.setFont(new Font("Sans Serif",Font.BOLD,16));
        f.add(send); 

        f.setSize(450,700);
        f.setLocation(800,50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(new Color(173,216,230));
        
        f.setVisible(true);
    }
    public void actionPerformed(ActionEvent ae){
        try{
            String out = text.getText();
            
            JPanel p2  = formatLabel(out);
            
            
            a1.setLayout(new BorderLayout());
            
            //Aligned at right side message sent
            JPanel right = new JPanel(new BorderLayout());
            right.add(p2,BorderLayout.LINE_END); //msg at end of line
            
            //Multiple messages sent vertically at right side
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15)); //height is 15 (space between 2 messages)

            // messages of vertical are added to a1 pannel as a1 will finally show the msgs and msg will be displayed from start of the page
            a1.add(vertical,BorderLayout.PAGE_START); 

            dout.writeUTF(out);

            //after sending msg text box is made empty
            text.setText("");

            //repaint the frame so that messages are displayed
            f.repaint();
            f.invalidate();
            f.validate();
        }
        catch(Exception e){
            System.out.print(e);
        }

    }

    public static JPanel formatLabel(String out){
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width:150px\">" + out + "</p></html>");
        output.setFont(new Font("Book Antiqua",Font.PLAIN,16));
        output.setBackground(new Color(102,255,102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15,15,15,50)); //padding left,top,bottom-15; right-50

        panel.add(output);

        //The getInstance() method in Calendar class is used to get a calendar using the current time zone and locale of the system.
        Calendar cal = Calendar.getInstance(); 

       //SimpleDateFormat class helps in formatting and parsing of data. We can change date from one format to other. It allows to user to interpret string date format into a Date object. 
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel(); //hard coding can't be done as time is fetched dynamically hence use setText
        time.setText(sdf.format(cal.getTime())); //getTime()-method used to get time and displays time in HH:mm form

        panel.add(time);

        return panel;
    }

    public static void main(String args[])
    {
        new Client();

        try{
            Socket s = new Socket("127.0.0.1",8000); //can be multiple sockets 127.0.0.1 -> local host and 8000 port no on which server is running
            DataInputStream din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            while(true)
            {
                a1.setLayout(new BorderLayout());
                String msg = din.readUTF();
                JPanel panel = formatLabel(msg);

                JPanel left = new JPanel(new BorderLayout());
                left.add(panel,BorderLayout.LINE_START);
                vertical.add(left);
                vertical.add(Box.createVerticalStrut(15));
                a1.add(vertical, BorderLayout.PAGE_START);

                f.validate();

            }
        }
        catch(Exception e){
            System.out.print(e);
        }
    }
}