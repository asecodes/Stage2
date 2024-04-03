package GUIrelative;

import Checkrelative.CheckMachine;
import Exceptionrelative.FormatException;
import Flightrelative.Flight;
import Passengerrelative.Passenger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;


public class GUI extends JFrame {
    private JButton button;
    private JTextField textField1;
    private JTextField textField2;
    private JLabel label1;
    private JLabel label2;

    public GUI() {
        // 初始化组件
        button = new JButton("Submit");
        textField1 = new JTextField(20);
        label1 = new JLabel("Enter your reference code:");
        textField2 = new JTextField(20);
        label2 = new JLabel("Enter your name:");

        // 设置布局管理器
        this.setLayout(new FlowLayout());

        // 创建两个面板用于容纳标签和文本字段
        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // 向面板中添加组件
        panel1.add(label1);
        panel1.add(textField1);
        panel2.add(label2);
        panel2.add(textField2);

        // 设置主布局管理器
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // 添加面板到窗口
        this.add(panel1);
        this.add(panel2);
        this.add(button);

        HashMap<String, Passenger> plist = null;
        try {
            plist = CheckMachine.readPassenger("PassengerList.csv");
        } catch (FormatException e) {
            throw new RuntimeException(e);
        }
        HashMap<String,Flight> flist = null;
        try {
            flist = CheckMachine.readFlight("FlightList.csv");
        } catch (FormatException e) {
            throw new RuntimeException(e);
        }
        // 添加按钮点击事件
        HashMap<String, Flight> finalFlist = flist;
        HashMap<String, Passenger> finalPlist = plist;
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String scanedreferencecode = textField1.getText();
                String scanedname = textField2.getText();
                try {
                    CheckMachine.checkInformation(scanedreferencecode,scanedname, finalPlist, finalFlist);
                } catch (FormatException ex) {

                }
            }
        });

        // 设置窗口的属性
        this.setTitle("Airport check-in software");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack(); // 自动调整大小
        this.setVisible(true); // 显示窗口

        // 添加窗口监听器
        HashMap<String, Flight> finalFlist1 = flist;
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 在这里添加你希望在窗口关闭时执行的代码
                performOnClose(finalFlist1);

                // 关闭窗口和应用程序
                dispose();
                System.exit(0);
            }
        });
    }

    private void performOnClose(HashMap<String, Flight> flist) {
        // 遍历 HashMap 的 values
        System.out.println("\n");
        System.out.println("Flightrelative.Flight List:\n");
        for (Flight flight : flist.values()) {
            System.out.println("\n");

            // 调用每个 Flightrelative.Flight 对象的 toString() 方法
            System.out.println(flight.toString());
        }
    }

    public void setTextField1(String text) {
        textField1.setText(text);
    }

    public void setTextField2(String text) {
        textField2.setText(text);
    }

    public void pressButton() {
        // 模拟点击按钮
        button.doClick();
    }

    public static void main(String[] args) {
        // 创建 GUIrelative.GUI 实例
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUI();
            }
        });
    }
}
