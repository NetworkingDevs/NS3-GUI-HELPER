import Dialogs.*;
import FileHandler.CodeGenerator;
import FileHandler.Reader;
import FileHandler.Writer;
import GuiHelpers.NodeHelper;
import GuiHelpers.P2PLinkHelper;
import GuiHelpers.TopologyPainter;
import StatusHelper.ToolStatus;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home_Screen extends JFrame {
    private final String FILE_MENU = "File";
    private final String SCENARIOS_MENU = "Scenarios";
    private final String SETTINGS_MENU = "Settings";
    private ArrayList<String> MenusOrder = new ArrayList<>();
    private final int MIN_WINDOW_WIDTH = 1090;
    private final int MIN_WINDOW_HEIGHT = 650;

    private JPanel JPanel_main;
    private JPanel JPanel_main_left;
    private JPanel JPanel_main_right;
    private JLabel lbl_info;
    private JScrollPane JScrollPane_canvas;
    private JButton btn_generateCode;
    private JScrollPane JScrollPane_forConfigJPanel;
    private JPanel JPanel_configTopology;
    private JPanel JPanel_tools;
    private JButton btn_tool_node;
    private JButton btn_tool_p2pConn;
    private JButton btn_tool_view;
    private JPanel JPanel_configTopo;
    private JPanel JPanel_grp_link;
    private JLabel lbl_link;
    private JButton btn_addLink;
    private JComboBox comboBox_links;
    private JPanel JPanel_grp_network;
    private JLabel lbl_network;
    private JButton btn_addNetwork;
    private JComboBox comboBox_networks;
    private JButton btn_serverConfig;
    private JButton btn_clientConfig;
    private JPanel JPanel_utils;
    private JCheckBox chkBox_wireshark;
    private JCheckBox chkBox_netanim;
    private JPanel JPanel_overview;
    private JLabel lbl_server;
    private JLabel lbl_client;
    private JLabel lbl_links;
    private JLabel lbl_networks;
    JMenuBar menuBar; // this is a menu bar
    Map<String, JMenu> menuMapping; // this is a mapping (from string to each menu), for ease in coding...
    Map<String, ArrayList<JMenuItem>> menuItemsListMapping; // this is a mapping (from string to list of each menu's item's list)

    // Images for tools and other buttons...
    Image imgInfo, icon_nodeTool, icon_p2pLinkTool, icon_viewTool;

    {
        try {
            imgInfo = ImageIO.read(getClass().getClassLoader().getResource("info.png"));
            icon_nodeTool = ImageIO.read(getClass().getClassLoader().getResource("icon_tool_node.png"));
            icon_p2pLinkTool = ImageIO.read(getClass().getClassLoader().getResource("icon_tool_p2pLink.png"));
            icon_viewTool = ImageIO.read(getClass().getClassLoader().getResource("icon_tool_view.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // creating the variables for serving functionalities ==============================================================
    TopologyPainter painter = new TopologyPainter(new ArrayList<>(), new ArrayList<>(), 600, 600);
    ToolStatus toolStatus;
    private int clicks = 1;
    private int firstNode = -1;
    Dialog_Link dialogLink;
    Dialog_Network dialogNetwork;
    Dialog_Connection dialogConnection;
    Dialog_ConfigureServer dialogConfigureServer;
    Dialog_ConfigureClient dialogConfigureClient;
    Dialog_outputFileChooser dialogOutputFileChooser;
    Writer writer;
    Reader reader;
    String OutputPath;
    CodeGenerator codeGenerator;
    public static JSONObject UNIVERSAL_SETTINGS;

    public Home_Screen() {
        // basic initialization of this component...
        // ========================================= BASIC CONF. =======================================================
        this.OutputPath = System.getProperty("user.dir");
        this.setContentPane(this.JPanel_main);
        this.setTitle("Topology Helper - NS3");
        this.setSize(1090,650);
        this.setMinimumSize(new Dimension(1090, 650));
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(800,600));

        this.menuMapping = new HashMap<>();
        this.menuItemsListMapping = new HashMap<>();

        this.lbl_info.setPreferredSize(new Dimension(550,50));
        this.lbl_info.setIcon(new ImageIcon(imgInfo.getScaledInstance(20,20,Image.SCALE_SMOOTH)));

        this.btn_tool_node.setIcon(new ImageIcon(icon_nodeTool.getScaledInstance(50,50, Image.SCALE_SMOOTH)));
        this.btn_tool_node.setText("");
        this.btn_tool_p2pConn.setIcon(new ImageIcon(icon_p2pLinkTool.getScaledInstance(50,50, Image.SCALE_SMOOTH)));
        this.btn_tool_p2pConn.setText("");
        this.btn_tool_view.setIcon(new ImageIcon(icon_viewTool.getScaledInstance(50,50, Image.SCALE_SMOOTH)));
        this.btn_tool_view.setText("");

        // TODO: move all those to create when clicking or it is used....
        this.dialogLink = new Dialog_Link(this.comboBox_links);
        this.dialogLink.setVisible(false);
        this.dialogNetwork = new Dialog_Network(this.comboBox_networks);
        this.dialogNetwork.setVisible(false);
        this.dialogConnection = new Dialog_Connection();
        this.dialogConnection.setVisible(false);
        this.dialogConfigureServer = new Dialog_ConfigureServer(0);
        this.dialogConfigureServer.setVisible(false);
        this.dialogConfigureClient = new Dialog_ConfigureClient(0);
        this.dialogConfigureClient.setVisible(false);
        this.dialogOutputFileChooser = new Dialog_outputFileChooser();
        this.dialogOutputFileChooser.setVisible(false);
        this.dialogOutputFileChooser.setPath(this.OutputPath);

        this.JScrollPane_canvas.setViewportView(this.painter);
        this.JScrollPane_forConfigJPanel.setViewportView(this.JPanel_configTopology);

        this.reader = new Reader(this.getClass().getClassLoader().getResource("Settings/file.txt"));
        this.reader.isFileEmpty();
        UNIVERSAL_SETTINGS = new JSONObject();
        UNIVERSAL_SETTINGS.put("outputPath", this.OutputPath);
        UNIVERSAL_SETTINGS.put("fileName", "output");
        // ========================================= BASIC CONF. =======================================================

        // Adding the menu bar to this component...
        this.setUpMenuBar();

        // setting up event listeners...
        this.setUpEventListeners();

        // all the settings and configuration should have been done before this call...
        this.setVisible(true);
    }

    private void addMenusToMenuBar() {
        for (String menu : this.MenusOrder) {
            this.menuBar.add(this.menuMapping.get(menu));
        }
    }

    private void addMenuItemsToMenus() {
        for (Map.Entry<String, JMenu> menu : this.menuMapping.entrySet()) {
            for (JMenuItem item : this.menuItemsListMapping.get(menu.getKey())) {
                menu.getValue().add(item);
            }
        }
    }

    private void incrementClicks(boolean successfulClick) {
        if (successfulClick) {
            clicks ++;
        }
    }

    private boolean checkLinkTool() {
        if (this.painter.getNodes().size() == 0) {
            JOptionPane.showMessageDialog(this, "Please add some nodes to make a connection!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (dialogLink.links.size() <= 1) {
            JOptionPane.showMessageDialog(this, "Please create at least one link before making a connection!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (dialogNetwork.links.size() <= 1) {
            JOptionPane.showMessageDialog(this, "Please create at least one network before making a connection!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    private boolean checkNodes(String msg) {
        if (painter.getNodes().size() == 0) {
            JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean checkNodes() {
        return this.checkNodes("Please add some nodes first!");
    }

    private void setUpMenuBar()
    {
        // ========================================= MENU BAR CONF. ====================================================
        this.menuBar = new JMenuBar();

        // adding each menu for menu bar...

        // step-1 : Add a menu in menus
        this.menuMapping.put(FILE_MENU,new JMenu("File"));
        // step-2 : create a menu item list
        this.menuItemsListMapping.put(FILE_MENU,new ArrayList<>());
        // step-3 : add menu items
        this.menuItemsListMapping.get(FILE_MENU).add(new JMenuItem("Output Path"));
        this.menuItemsListMapping.get(FILE_MENU).get(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogOutputFileChooser.setVisible(true);
            }
        });
        // step-4 : add menu to menu order
        this.MenusOrder.add(FILE_MENU);
        // following above 4 steps for each menu...

        // for scenarios menu...
        this.menuMapping.put(SCENARIOS_MENU, new JMenu("Scenarios"));
        this.menuItemsListMapping.put(SCENARIOS_MENU, new ArrayList<>());
        this.menuItemsListMapping.get(SCENARIOS_MENU).add(new JMenuItem("Basic P2P"));
        this.menuItemsListMapping.get(SCENARIOS_MENU).add(new JMenuItem("Basic Ring - 3 Nodes"));
        this.menuItemsListMapping.get(SCENARIOS_MENU).add(new JMenuItem("Basic Mesh - 4 Nodes"));
        this.menuItemsListMapping.get(SCENARIOS_MENU).add(new JMenuItem("Basic Star - 5 Nodes"));
        this.MenusOrder.add(SCENARIOS_MENU);

        // for settings menu...
        this.menuMapping.put(SETTINGS_MENU, new JMenu("Settings"));
        this.menuItemsListMapping.put(SETTINGS_MENU, new ArrayList<>());
        this.menuItemsListMapping.get(SETTINGS_MENU).add(new JMenuItem("Default Server Config"));
        this.menuItemsListMapping.get(SETTINGS_MENU).add(new JMenuItem("Default Client Config"));
        this.MenusOrder.add(SETTINGS_MENU);


        this.addMenuItemsToMenus();
        this.addMenusToMenuBar();
        this.setJMenuBar(menuBar);
        // ========================================= MENU BAR CONF. ====================================================
    }

    private void updateServerIndex(String lbl) {
        this.lbl_server.setText("Server Index : "+lbl);
    }

    private void updateClientIndex(String lbl) {
        this.lbl_client.setText("Client Index : "+lbl);
    }

    private void updateLinkCount(String lbl) {
        this.lbl_links.setText("Links : "+lbl);
    }

    private void updateNetworkCount(String lbl) {
        this.lbl_networks.setText("Networks : "+lbl);
    }

    private void showServerConfigWarning() {
        JOptionPane.showMessageDialog(this, "Please configure the server first!", "Warning!", JOptionPane.WARNING_MESSAGE);
    }

    private void setUpEventListeners()
    {
        // ========================================= ALL EVENT LISTENERS ===============================================
        // record each mouse click on canvas...
        // do accordingly...
        this.painter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO: Use Switch Case
                super.mouseClicked(e);
                System.out.println("Clicked at x : "+e.getX()+" y : "+e.getY());
                // if tool is 'node' tool then...
                if (toolStatus == ToolStatus.TOOL_NODE) {
                    // create a new node at given point...
                    NodeHelper node = new NodeHelper(e.getX()-10, e.getY()-10, 20,String.valueOf(painter.getNodes().size()));
                    // add that node to painter for painting on canvas...
                    painter.addAndPrintNode(node);
                }
                // if tool is 'link' tool then...
                else if (toolStatus == ToolStatus.TOOL_LINK) {
                    int collision = -1;
                    boolean successfulClick = false;
                    // for first successful click....
                    // check if collision with any node...
                    collision = painter.pointCollideWithAny(e.getX(), e.getY());
                    // System.out.println("Collision : "+collision+" Clicks : "+clicks); // just for testing...
                    if (collision >= 0 && clicks%2!=0) {
                        // if collision then change the information label and increment the no. of clicks...
                        firstNode = collision;
                        lbl_info.setText("Connection Tool Selected: First node selected now, select second node.");
                        successfulClick = true;
                    }

                    // for second successful click...
                    // check if collision with any node...
                    if (collision >= 0 && clicks%2==0) {
                        // if collision then change the information label, increment the no. of clicks and open a device configuration dialog box...
                        lbl_info.setText("Connection Tool Selected: To create a link, click on two nodes sequentially.");
                        painter.addAndPrintLink(new P2PLinkHelper(painter.getNodes().get(firstNode), painter.getNodes().get(collision)));
                        successfulClick = true;
                        System.out.println(firstNode+" "+collision);
                        dialogConnection.showDialog(dialogLink.links,dialogNetwork.links,firstNode,collision);
                        firstNode = -1;
                    }
                    incrementClicks(successfulClick);
                }
                // if tool is 'view' tool...
                else if (toolStatus == ToolStatus.TOOL_VIEW) {

                }
            }
        });

        // action to perform when node tool is selected...
        btn_tool_node.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lbl_info.setText("Node Tool Selected: Click anywhere on the canvas to add a node.");
                toolStatus = ToolStatus.TOOL_NODE;
            }
        });

        // action to be performed when link tool is selected...
        btn_tool_p2pConn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check condition if there exists at least one link and warn if not present...(pending)
                if (checkLinkTool()) {
                    lbl_info.setText("Connection Tool Selected: To create a link, click on two nodes sequentially.");
                    toolStatus = ToolStatus.TOOL_LINK;
                }
            }
        });

        // event that will happen immediately when clicking on view tool
        btn_tool_view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkNodes("WARNING : No nodes to view!!")) {
                    lbl_info.setText("View Tool Selected : Double click to disable! OR Click on any node to view connections!");
                    int server_index = Integer.MIN_VALUE,client_index = Integer.MIN_VALUE;
                    if (dialogConfigureServer.settings.size() != 0) {
                        server_index = Integer.parseInt(dialogConfigureServer.settings.get(0));
                    }

                    if (dialogConfigureClient.settings.size() != 0) {
                        client_index = Integer.parseInt(dialogConfigureClient.settings.get(0));
                    }
                    painter.enableView(server_index,client_index);
                }
            }
        });

        btn_tool_view.addMouseListener(new MouseAdapter() {
            private long lastClick = 0;

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                long currentClick = System.currentTimeMillis();
                if (currentClick - lastClick < 500) {
                    painter.enableView(-1,-1);
                    lbl_info.setText("Select the tool to make your toplogy!");
                }
                lastClick = currentClick;
            }
        });

        // action to be performed when Add Link button is pressed...
        btn_addLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogLink.setVisible(true);
            }
        });
        // action to be performed when Add Network button is pressed...
        btn_addNetwork.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogNetwork.setVisible(true);
            }
        });


        // action to be performed when clicking on configure server button...
        btn_serverConfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkNodes()) {
                    dialogConfigureServer.showDialog(painter.getNodes().size());
                }
            }
        });
        // action to be performed when clicking on configure client button...
        btn_clientConfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkNodes()) {
                    if (dialogConfigureServer.settings.size() == 0) { // server configuration has not been done yet...
                        // show warning message...
                        showServerConfigWarning();
                    } else { // otherwise...
                        // show client configuration dialog box...
                        dialogConfigureClient.showDialog(painter.getNodes().size());
                    }
                }
            }
        });

        // action to be performed when clicking on generate code button...
        btn_generateCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkNodes("Please create your topology first!")) {
                    createFile();
                }
            }
        });

        // update server index in overview tab when server configuration dialog is hidden...
        this.dialogConfigureServer.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
                if (dialogConfigureServer.settings.size() != 0) {
                    updateServerIndex("configured node "+dialogConfigureServer.settings.get(0));
                }
            }
        });

        // update client index in overview tab when client configuration dialog is hidden...
        this.dialogConfigureClient.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
                if (dialogConfigureClient.settings.size() != 0) {
                    updateClientIndex("configured node "+dialogConfigureClient.settings.get(0));
                }
            }
        });

        // update links count in overview tab when add link dialog is closed.....
        this.dialogLink.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
                if (dialogLink.links.size() > 1) {
                    updateLinkCount(dialogLink.links.size()-1+" links created");
                }
            }
        });

        // update n/w count in overview tab when add n/w dialog is closed.....
        this.dialogNetwork.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
                if (dialogNetwork.links.size() > 1) {
                    updateNetworkCount(dialogNetwork.links.size()-1+" n/w created");
                }
            }
        });

        // this code was just for testing...
        // now I am using this code for maintaining minimum size of the window...
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                // Observing output while testing....
                // System.out.println("Width : "+getWidth()+" Height : "+getHeight());
                int w = getWidth(), h=getHeight();
                if (w < MIN_WINDOW_WIDTH || h < MIN_WINDOW_HEIGHT) {
                    setSize(MIN_WINDOW_WIDTH,MIN_WINDOW_HEIGHT);
                }
            }
        });
        // ==================================== ALL EVENT LISTENERS ENDS ===============================================
    }

    private void createFile() {
        this.writer = new Writer(this.OutputPath);

        Map<String, String> otherFields = new HashMap<>();
        if (this.chkBox_netanim.isSelected()) {
            otherFields.put(CodeGenerator.UTILITY_NETANIM, CodeGenerator.VALUE_TRUE);
        } else {
            otherFields.put(CodeGenerator.UTILITY_NETANIM, CodeGenerator.VALUE_FALSE);
        }

        if (this.chkBox_wireshark.isSelected()) {
            otherFields.put(CodeGenerator.UTILITY_WIRESHARK,CodeGenerator.VALUE_TRUE);
        } else {
            otherFields.put(CodeGenerator.UTILITY_WIRESHARK,CodeGenerator.VALUE_FALSE);
        }

        otherFields.put(CodeGenerator.NAME_OF_TOPOLOGY, "Custom");
        otherFields.put(CodeGenerator.TOTAL_NODES, String.valueOf(this.painter.getNodes().size()));
        this.codeGenerator = new CodeGenerator(dialogConfigureServer,dialogConfigureClient,dialogConnection,dialogLink, otherFields);
        this.codeGenerator.GenerateCode();
        this.writer.writeToFile(this.codeGenerator.getCode());
        this.writer.closeTheFile();

        JOptionPane.showMessageDialog(this, "File has been generated successfully!\nAt : "+this.OutputPath, "Code Generated!", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new Home_Screen();
    }
}
