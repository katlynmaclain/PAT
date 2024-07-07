import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class ButtonExample {

    ButtonExample() { // constructor = method yang pertama kali dijalankan, namanya sm dengan classnya
        JFrame frame = new JFrame("Contoh frame dengan button");

        JTextField tf1 = new JTextField();
        tf1.setBounds(50, 10, 150, 30);

        JButton b1 = new JButton("Klik di sini"); //button biasa
        b1.setBounds(50, 50, 100, 30);
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tf1.setText("Button diklik");
                // throw new UnsupportedOperationException("Unimplemented method
                // 'actionPerformed'");
            }
        });

        JButton b2 = new JButton("Klik di sini", new ImageIcon("click.png")); //button gambar + tulisan
        b2.setBounds(50, 100, 200, 30);
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tf1.setText("Button image diklik");
                // throw new UnsupportedOperationException("Unimplemented method
                // 'actionPerformed'");
            }
        });

        frame.add(b1);
        frame.add(b2);
        frame.add(tf1);
        frame.setSize(640, 480);
        frame.setLayout(null);
        frame.setVisible(true); // harus ada kalau ndak ada, ndak tampil framenya
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // biar kalau framenya disilang, nanti lgsg keluar dari
                                                              // program
    }

    public static void main(String[] args) {
        new ButtonExample();
    }
}