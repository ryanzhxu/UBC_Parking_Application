package ui;

import model.*;
import network.ReadWebPageEx;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainFrame extends JPanel {

    static final int HORIZONTAL_SCROLL = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
    static final ImageIcon icon = new ImageIcon("9_2016_3UnitNarrow_CompSci_Blue282RGB300.jpg");

    static Parking indoorParking = new IndoorParking("Indoor Parking");
    static Parking outdoorParking = new OutdoorParking("Outdoor Parking");
    static Parking parking;
    static Vehicle currVehicle;
    static Stall currStall;
    static JButton park = new JButton("Park");
    static JButton leave = new JButton("Leave");
    static JButton quit = new JButton("Quit");
    static JButton total = new JButton("See Total");
    static JButton save = new JButton("Save");
    static JTextArea Console = new JTextArea(30, 30);
    static JTextArea listCars = new JTextArea("List of Cars Currently Parked:\n\n", 30, 30);
    static String filename;
    static String currPlateNum;
    static File file;
    static JFrame frame;
    static JPanel centerPanel;
    static Date today = Calendar.getInstance().getTime();
    static Calendar cal = Calendar.getInstance();
    static ArrayList<Integer> stalls = new ArrayList<>();
    static ArrayList<String> licensePlates = new ArrayList<>();
    static Integer currStallNum;
    static String[] options = {"Indoor Parking", "Outdoor Parking"};
    PrintStream printStream = new PrintStream(new FilteredStream(new ByteArrayOutputStream()));

    class FilteredStream extends FilterOutputStream {
        public FilteredStream(OutputStream stream) {
            super(stream);
        }

        public void write(byte[] b) throws IOException {
            String str = new String(b);
            Console.append(str);
        }

        public void write(byte[] b, int off, int len) throws IOException {
            String str = new String(b, off, len);
            Console.append(str);
            FileWriter writer = new FileWriter("a.log", true);
            writer.write(str);
            writer.close();
        }
    }

    private static void makeParkWork() {
        park.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("true")) {
                    try {
                        promptForPark();
                    } catch (FullParkException ex) {
                        JOptionPane.showMessageDialog(null, "This parking is full...",
                                "FullParkException", JOptionPane.ERROR_MESSAGE);
                    } catch (ParkedStallException ex) {
                        JOptionPane.showMessageDialog(null, "This stall has been parked...",
                                "ParkedStallException", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private static void makeLeaveWork() {
        leave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("true")) {
                    try {
                        tryPromptForLeave();
                    } catch (EmptyParkException ex) {
                        JOptionPane.showMessageDialog(null, "Parking is empty...",
                                "EmptyParkException", JOptionPane.ERROR_MESSAGE);
                    } catch (VehicleNotFoundException ex) {
                        JOptionPane.showMessageDialog(null, "Vehicle not found...",
                                "VehicleNotFoundException", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private static void makeSaveWork() {
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    performSave();
                    if (filename == null) {
                        JOptionPane.showMessageDialog(null, "File has not been saved...",
                                "Notification Window", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "File has been saved at " + filename,
                                "Notification Window", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private static void performSave() throws IOException {
        if (selectParking()) {
            Integer res = JOptionPane.showConfirmDialog(null, "Write in default file?",
                    "Selecting file path...", JOptionPane.YES_NO_OPTION);
            if (res != null) {
                if (res == 0) {
                    parking.save();
                    filename = "outputDefault.txt";
                } else if (res == 1) {
                    promptForFileName();
                }
            }
        }
    }

    private static void promptForFileName() throws IOException {
        filename = JOptionPane.showInputDialog("Please enter the file name:");
        if (filename != null) {
            file = new File(filename);
            while (!file.exists()) {
                filename = JOptionPane.showInputDialog("Please enter the file name:");
                if (filename == null) {
                    break;
                }
                file = new File(filename);
            }
        }
    }

    private static boolean selectParking() {

        int x = JOptionPane.showOptionDialog(frame, "Please choose: ",
                "Selecting Parking...",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (x == 0) {
            parking = indoorParking;
            return true;
        } else if (x == 1) {
            parking = outdoorParking;
            return true;
        }
        return false;
    }

    private static void tryPromptForLeave() throws EmptyParkException, VehicleNotFoundException {

        // avoid error when cancelled
        if (selectParking()) {
            if (parking.isEmpty()) {
                JOptionPane.showMessageDialog(null, "This parking is empty...",
                        "EmptyParkException", JOptionPane.ERROR_MESSAGE);
            } else {
                promptForLeave();
            }
        }
    }

    private static void promptForPark() throws FullParkException, ParkedStallException {
        // avoid error when cancelled
        if (selectParking()) {
            promptForVehicle();
            if (currVehicle != null) {
                promptForStall();
                if (currStall != null) {
                    parking.addVehicle(currStall, currVehicle);
                    promptForDuration();
                }
            }

            // reset pointer variables
            currStall = null;
            currVehicle = null;
        }
    }

    private static void promptForVehicle() {
        currPlateNum = JOptionPane.showInputDialog("Please enter your vehicle's license plate number:");
        if (currPlateNum != null) {
            currVehicle = new Vehicle(currPlateNum);
//            licensePlates.add(currPlateNum);
        }
    }

    private static void promptForStall() {
        String strStallNum = JOptionPane.showInputDialog("Please enter the stall number:");
        if (strStallNum != null) {
            currStallNum = Integer.parseInt(strStallNum);
            if (currStallNum != null) {
                while (currStallNum < 1 || currStallNum > 300) {
                    currStallNum = Integer.parseInt(JOptionPane.showInputDialog("Invalid stall...Please try again:"));
                }
                while (stalls.contains(currStallNum)) {
                    currStallNum = Integer.parseInt(JOptionPane.showInputDialog("Stall's been parked...Please try again:"));
                }
                currStall = new Stall(currStallNum);
//                stalls.add(currStallNum);
            }
        }
    }


    private static void promptForLeave() throws EmptyParkException, VehicleNotFoundException {
        currPlateNum = JOptionPane.showInputDialog("Please enter your vehicle's plate number:");
        if (currPlateNum != null) {
            currVehicle = new Vehicle(currPlateNum);
            while (!parking.vehicleExist(currVehicle)) {
                currPlateNum = JOptionPane.showInputDialog("Vehicle not found...Please try again:");
                if (currPlateNum == null) {
                    break;
                }
            }
            currVehicle = new Vehicle(currPlateNum);
            parking.removeVehicle(currVehicle);
//            licensePlates.remove(currPlateNum);
//            stalls.remove(currStallNum);
            JOptionPane.showMessageDialog(null, currVehicle.getLicensePlate() + " has been removed from "
                    + parking.getParkingName(), "Notification", JOptionPane.INFORMATION_MESSAGE);
            currStall = null;
            currVehicle = null;
        }
    }

    private static void makeQuitWork() {
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the program",
                        "Exit Program?", JOptionPane.YES_NO_OPTION);
                if (confirmed == JOptionPane.YES_OPTION) {
                    frame.dispose();
                }
            }
        });
    }

    private static void makeTotalWork() {
        total.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectParking()) {
                    JOptionPane.showMessageDialog(null, "Total parking space remaining: "
                            + parking.getTotalFreeSpace(), "Check total space", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private void addButtons() {
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));

        placeButtons();

        frame.add(centerPanel);
        setUpBorder();
    }

    private static void placeButtons() {
        centerPanel.add(park);
        park.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        centerPanel.add(leave);
        leave.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        centerPanel.add(total);
        total.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        centerPanel.add(quit);
        quit.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        centerPanel.add(save);
        save.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 40)));
    }

    private void setUpBorder() {
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setFontAndScroll();
    }

    private void setFontAndScroll() {
        Console.setColumns(30);
        Console.getCaret().setBlinkRate(530);
        Console.setFont(new Font("Arial", Font.PLAIN, 18));
        Console.setForeground(Color.BLACK);
        listCars.setFont(new Font("Arial", Font.PLAIN, 18));
        listCars.setForeground(Color.BLACK);
        listCars.setEditable(false);
        add(new JScrollPane(Console, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLL));
        add(centerPanel);
        add(new JScrollPane(listCars, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLL));
    }

    public MainFrame() {
        addButtons();
        System.setOut(printStream);
        System.setErr(printStream);
    }

    // Method for showing GUI
    public static void showGUI() throws IOException {
        frame = new JFrame("App");
        Dimension d1 = new Dimension(960, 600);
        frame.setPreferredSize(d1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainFrame());
        WelcomeMessage w = WelcomeMessage.getInstance();
        new ReadWebPageEx();
        park.setActionCommand("true");
        leave.setActionCommand("true");
        makeButtonsAllWork();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        JOptionPane.showMessageDialog(frame, icon, "Welcome Screen", JOptionPane.INFORMATION_MESSAGE, null);
        JOptionPane.showMessageDialog(frame, "Welcome to UBCPark!", "Program Welcome Message",
                JOptionPane.INFORMATION_MESSAGE, null);
    }

    private static void makeButtonsAllWork() {
        makeParkWork();
        makeLeaveWork();
        makeQuitWork();
        makeTotalWork();
        makeSaveWork();
    }

    private static void promptForDuration() {
        String strDuration = JOptionPane.showInputDialog("How long are you planning to stay: (in hours)");
        if (strDuration != null) {
            Integer duration = Integer.parseInt(strDuration);
            while (duration >= 24) {
                duration = Integer.parseInt(JOptionPane.showInputDialog("Invalid time...Please try again:"));
            }
            printInvoice(duration);
            listCars.append(currVehicle.getLicensePlate() + " ");
            listCars.append(currVehicle.getStall().getStallNum() + " " + parking.getParkingName() + "\n");
        }
    }

    private static String invoiceFullDay() {
        return "Invoice\n" + currVehicle.getLicensePlate() + "\nExpiration Time:"
                + "\n11:59 PM" + "\n" + today + "\nTotal due: $"
                + 5 * parking.getRate();
    }

    private static String invoiceTimed(int duration) {
        cal.add(Calendar.HOUR_OF_DAY, +duration);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        return "Invoice\n" + currVehicle.getLicensePlate() + "\nExpiration Time:\n"
                + hour + ":" + min + today + "\nTotal Due: $"
                + duration * parking.getRate();

    }

    private static void printInvoice(int duration) {
        if (duration >= 5) {
            JOptionPane.showMessageDialog(frame, invoiceFullDay());
        } else {
            JOptionPane.showMessageDialog(frame, invoiceTimed(duration));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ReadWebPageEx();
                    showGUI();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
