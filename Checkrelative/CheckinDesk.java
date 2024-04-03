package Checkrelative;

import GUIrelative.GUI;
import Observer_model.Observable_passengers_line;
import Observer_model.Observer;
import Passengerrelative.Passenger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CheckinDesk extends Thread {
    private int deskID;
    private GUI myGUI;
    private Observer passenger;

    private boolean isRunning;



    private static final Object lock = new Object();


    public int getDeskID() {
        return deskID;
    }

    public void setDeskID(int deskID) {
        this.deskID = deskID;
    }

    public GUI getMyGUI() {
        return myGUI;
    }

    public void setMyGUI(GUI myGUI) {
        this.myGUI = myGUI;
    }

    public Observer getPassenger() {
        return passenger;
    }

    public void setPassenger(Observer passenger) {
        this.passenger = passenger;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    // 构造函数，接收一个任务ID来初始化TaskExecutor实例。
    public CheckinDesk(int deskID) {

        this.deskID = deskID;



    }

    public void initGUI() {
        myGUI = new GUI(); // 使用一个特定的方法来初始化GUI
    }

    public void run() {

        try {
            setRunning(true);
            if(isRunning) {
                System.out.println("CheckinDesk " + deskID + " is checking in for boarding, please be patient or search for available CheckinDesk.\n");
            }
            initGUI();
            myGUI.setTextField1(getPassenger().getReferenceCode()); // 设置textField1
            myGUI.setTextField2(getPassenger().getName()); // 设置textField2
            myGUI.pressButton(); // 模拟点击提交按钮
            // 模拟耗时操作，线程休眠2000毫秒。
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
            Thread.currentThread().interrupt(); // 重新设置中断状态
        } finally {
            // 在任务执行完成后，无论成功还是异常，都将isRunning的状态设置为false。
            setRunning(false);
            if(!isRunning) {
                System.out.println("CheckinDesk " + deskID + " is available now.\n");

                }

            }


        }



    public static void main(String[] args) {
        Observable_passengers_line passengersLine = new Observable_passengers_line();

        Queue<Observer> passengers = passengersLine.getinqueue();


        ExecutorService executor = Executors.newFixedThreadPool(5);
        CheckinDesk[] desk = new CheckinDesk[5];

        long startTime = System.currentTimeMillis(); // 记录开始时间
        System.out.println("Check-in start time："+ startTime+"\n");




        //System.out.println("开始执行！");

        // 初始化Task对象，并提交到线程池执行。


        while(!passengers.isEmpty()&& !executor.isShutdown()) {
            for (int i = 0; i < 5; i++) {

                if((System.currentTimeMillis() - startTime)>=15*1000){
                    System.out.println("Dear all passengers,attention please!All check-in processes will cease soon in 15 seconds!\n");
                    executor.shutdown();
                    try{
                        // 等待一段时间，让已经提交的任务有机会完成执行
                        Thread.sleep(15000); // 暂停15秒
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }finally{
                        System.out.println("Dear all passengers,attention please!All check-in processes have ceased.\n");
                    }
                    break; // 添加这行代码来跳出外层循环
                }

                desk[i] = new CheckinDesk(i);
                desk[i].setPassenger(passengers.poll());
                executor.submit(desk[i]);
                try{
                    Thread.sleep(2000); // 暂停两秒
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Current time："+ System.currentTimeMillis()+"\n");



            }
        }




    }
}


