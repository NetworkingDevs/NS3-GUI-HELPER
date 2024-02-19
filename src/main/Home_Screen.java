import Dialogs.*;
import FileHandler.CodeGenerator;
import FileHandler.FileReaderWriter;
import GuiRenderers.CsmaLinkPainter;
import GuiRenderers.NodePainter;
import GuiRenderers.P2pLinkPainter;
import GuiRenderers.TopologyPainter;
import Helpers.DebuggingHelper;
import Netowkrs.Network;
import Links.NetworkLink;
import StatusHelper.ToolStatus;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static Helpers.ApplicationSettingsHelper.*;

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
    private JScrollPane JScrollPane_forTools;
    private JPanel JPanel_tools_buttons;
    private JButton btn_tool_csmaConn;
    JMenuBar menuBar; // this is a menu bar
    Map<String, JMenu> menuMapping; // this is a mapping (from string to each menu), for ease in coding...
    Map<String, ArrayList<JMenuItem>> menuItemsListMapping; // this is a mapping (from string to list of each menu's item's list)
    ArrayList<Integer> nodesInCsma = new ArrayList<>();

    // Images for tools and other buttons...
    Image imgInfo, icon_nodeTool, icon_p2pLinkTool, icon_csmaLinkTool, icon_viewTool, icon_selected;

    {
        try {
            imgInfo = ImageIO.read(getClass().getClassLoader().getResource("info.png"));
            icon_nodeTool = ImageIO.read(getClass().getClassLoader().getResource("icon_tool_node.png"));
            icon_p2pLinkTool = ImageIO.read(getClass().getClassLoader().getResource("icon_tool_p2pLink.png"));
            icon_csmaLinkTool = ImageIO.read(getClass().getClassLoader().getResource("icon_tool_csmaLink.png"));
            icon_viewTool = ImageIO.read(getClass().getClassLoader().getResource("icon_tool_view.png"));
            icon_selected = ImageIO.read(getClass().getClassLoader().getResource("icon_selected.png"));
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
    Dialog_Helper dialogHelper;
    Dialog_DefaultLinkConfig dialogDefaultLinkConfig;
    Dialog_DefaultNetworkConfig dialogDefaultNetworkConfig;
    String OutputPath;
    CodeGenerator codeGenerator;

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

        this.btn_tool_node.setIcon(new ImageIcon(icon_nodeTool.getScaledInstance(50,50, Image.SCALE_AREA_AVERAGING)));
        this.btn_tool_node.setText("");
        this.btn_tool_p2pConn.setIcon(new ImageIcon(icon_p2pLinkTool.getScaledInstance(50,50, Image.SCALE_AREA_AVERAGING)));
        this.btn_tool_p2pConn.setText("");
        this.btn_tool_csmaConn.setIcon(new ImageIcon(icon_csmaLinkTool.getScaledInstance(50,50, Image.SCALE_AREA_AVERAGING)));
        this.btn_tool_csmaConn.setText("");
        this.btn_tool_view.setIcon(new ImageIcon(icon_viewTool.getScaledInstance(50,50, Image.SCALE_AREA_AVERAGING)));
        this.btn_tool_view.setText("");

        this.dialogHelper = new Dialog_Helper(this.JPanel_main);

        this.JScrollPane_canvas.setViewportView(this.painter);
        this.JScrollPane_forConfigJPanel.setViewportView(this.JPanel_configTopology);

        setUpSettings();
        // ========================================= BASIC CONF. =======================================================

        // Adding the menu bar to this component...
        this.setUpMenuBar();

        // setting up event listeners...
        this.setUpEventListeners();

        // all the settings and configuration should have been done before this call...
        this.setVisible(true);
    }

    private void addMenusToMenuBar() {
        for (String menu : this.MenusOrder) { // adding each menu to menu bar...
            this.menuBar.add(this.menuMapping.get(menu));
        }
    }

    private void addMenuItemsToMenus() {
        for (Map.Entry<String, JMenu> menu : this.menuMapping.entrySet()) { // for each menu...
            for (JMenuItem item : this.menuItemsListMapping.get(menu.getKey())) { // adding each menu item...
                menu.getValue().add(item);
            }
        }
    }

    private void incrementClicks(boolean successfulClick) {
        if (successfulClick) {
            clicks ++;
        }
    }

    private boolean checkIfLinkCanBeAdded(ToolStatus toolStatus) {
        instantiateLinkDialog();
        DebuggingHelper.Debugln(dialogLink.getP2pLinkCount()+" CSMA Link Count : "+dialogLink.getCsmaLinkCount());
        // link tool selection is invalid in below conditions...
        if (this.painter.getNodes().size() == 0) { // if there are no node...
            this.dialogHelper.showWarningMsg("Please add some nodes to make a connection!", "Warning");
            return false;
        } else if ((toolStatus == ToolStatus.TOOL_LINK)?(dialogLink.getP2pLinkCount()==0):((toolStatus == ToolStatus.TOOL_LINK_CSMA)?(dialogLink.getCsmaLinkCount()==0):(false))) { // if there are no links...
            this.dialogHelper.showWarningMsg("Please create at least one link before making a connection!", "Warning");
            return false;
        } else if (dialogNetwork==null || (dialogNetwork.links.size() == 0 && dialogNetwork.defaultNetworks.size() == 0)) { // if there are no network settings made...
            this.dialogHelper.showWarningMsg("Please create at least one network before making a connection!", "Warning");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkIfNodesExists (String msg) {
        if (painter.getNodes().size() == 0) {
            this.dialogHelper.showErrorMsg(msg, "Error!");
            return false;
        }
        return true;
    }

    private void setUpMenuBar() {
        // ========================================= MENU BAR CONF. ====================================================
        this.menuBar = new JMenuBar();

        // adding each menu for menu bar...

        // step-1 : Add a menu in menus
        this.menuMapping.put(FILE_MENU, new JMenu("File"));
        // step-2 : create a menu item list
        this.menuItemsListMapping.put(FILE_MENU, new ArrayList<>());
        // step-3 : add menu items
        this.menuItemsListMapping.get(FILE_MENU).add(new JMenuItem("Output Path"));
        this.menuItemsListMapping.get(FILE_MENU).get(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dialogOutputFileChooser != null) {
                    dialogOutputFileChooser.setPath(UNIVERSAL_SETTINGS.get(OUTPUT_PATH).toString());
                    dialogOutputFileChooser.setFileName(UNIVERSAL_SETTINGS.get(FILE_NAME).toString());
                    dialogOutputFileChooser.setVisible(true);
                } else {
                    dialogOutputFileChooser = new Dialog_outputFileChooser();
                    dialogOutputFileChooser.setPath(UNIVERSAL_SETTINGS.get(OUTPUT_PATH).toString());
                    dialogOutputFileChooser.setFileName(UNIVERSAL_SETTINGS.get(FILE_NAME).toString());
                    dialogOutputFileChooser.addComponentListener(new ComponentAdapter() {
                        @Override
                        public void componentHidden(ComponentEvent e) {
                            super.componentHidden(e);
                            DebuggingHelper.Debugln("Output settings has been altered!");
                            UNIVERSAL_SETTINGS.remove(OUTPUT_PATH);
                            UNIVERSAL_SETTINGS.put(OUTPUT_PATH, dialogOutputFileChooser.getOutputPath());
                            UNIVERSAL_SETTINGS.remove(FILE_NAME);
                            UNIVERSAL_SETTINGS.put(FILE_NAME, dialogOutputFileChooser.getFileName());
                            DebuggingHelper.Debugln("Updated JSON : " + UNIVERSAL_SETTINGS.toString());
                        }
                    });
                    dialogOutputFileChooser.setVisible(true);
                }
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
        // this.MenusOrder.add(SCENARIOS_MENU);

        // for settings menu...
        this.menuMapping.put(SETTINGS_MENU, new JMenu("Settings"));
        this.menuItemsListMapping.put(SETTINGS_MENU, new ArrayList<>());
        this.menuItemsListMapping.get(SETTINGS_MENU).add(new JMenuItem("Default Link Config"));
        this.menuItemsListMapping.get(SETTINGS_MENU).get(0).addActionListener(e -> {
            instantiateLinkDialog();
            if (Dialog_Link.SHOW_DEFAULT) {
                int choice = dialogHelper.showConfirmationDialog("Your default links will be hidden temporarily!\nHowever, you can make them visible again.\nYou want to continue?","Warning!");
                if (choice == JOptionPane.YES_OPTION) {
                    instantiateDefaultLinkConfig();
                    dialogDefaultLinkConfig.setVisible(true);
                    dialogDefaultLinkConfig.showLinksAgain();
                    dialogLink.showDefaultLinks(false);
                    menuItemsListMapping.get(SETTINGS_MENU).get(1).setText("Show default links");
                    menuItemsListMapping.get(SETTINGS_MENU).get(1).setIcon(null);
                }
            } else {
                instantiateDefaultLinkConfig();
                dialogDefaultLinkConfig.setVisible(true);
                dialogDefaultLinkConfig.showLinksAgain();
            }
        });
        this.menuItemsListMapping.get(SETTINGS_MENU).add(new JMenuItem("Show default links"));
        this.menuItemsListMapping.get(SETTINGS_MENU).get(1).addActionListener(new ActionListener() {
            boolean iconVis = (menuItemsListMapping.get(SETTINGS_MENU).get(1).getIcon()==null)?(false):(true);
            JMenuItem THIS = menuItemsListMapping.get(SETTINGS_MENU).get(1);

            @Override
            public void actionPerformed(ActionEvent e) {
                iconVis = (THIS.getIcon()==null)?(false):(true);
                instantiateDefaultLinkConfig();
                instantiateLinkDialog();
                if (dialogDefaultLinkConfig.defaultLinks.size() == 0) {
                    dialogHelper.showWarningMsg("None default links are configured!", "Warning!");
                } else {
                    dialogLink.setDefaultLinks(dialogDefaultLinkConfig.defaultLinks);
                    if (iconVis) {
                        DebuggingHelper.Debugln("Hiding the default links!");
                        THIS.setIcon(null);
                        THIS.setText("Show default links");
                        dialogLink.showDefaultLinks(false);
                    } else {
                        DebuggingHelper.Debugln("Showing the default links!");
                        THIS.setIcon(new ImageIcon(icon_selected.getScaledInstance(8, 8, Image.SCALE_SMOOTH)));
                        THIS.setText("Hide default links");
                        dialogLink.showDefaultLinks(true);
                        dialogHelper.showInformationMsg("Default links are visible!", "Success!");
                    }
                    iconVis = !iconVis;
                }
            }
        });
        this.menuItemsListMapping.get(SETTINGS_MENU).add(new JMenuItem("Default Network Config"));
        this.menuItemsListMapping.get(SETTINGS_MENU).get(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                instantiateNetworkDialog();
                if (Dialog_Network.SHOW_DEFAULT) {
                    int choice = dialogHelper.showConfirmationDialog("Your default networks will be hidden temporarily!\nHowever, you can make them visible again.\nYou want to continue?","Warning!");
                    if (choice == JOptionPane.YES_OPTION) {
                        instantiateDefaultNetworkConfig();
                        dialogDefaultNetworkConfig.setVisible(true);
                        dialogDefaultNetworkConfig.showNetworksAgain();
                        dialogNetwork.showDefaultNetworks(false);
                        menuItemsListMapping.get(SETTINGS_MENU).get(3).setText("Show default networks");
                        menuItemsListMapping.get(SETTINGS_MENU).get(3).setIcon(null);
                    }
                } else {
                    instantiateDefaultNetworkConfig();
                    dialogDefaultNetworkConfig.setVisible(true);
                    dialogDefaultNetworkConfig.showNetworksAgain();
                }
            }
        });
        this.menuItemsListMapping.get(SETTINGS_MENU).add(new JMenuItem("Show default networks"));
        this.menuItemsListMapping.get(SETTINGS_MENU).get(3).addActionListener(new ActionListener() {
            boolean iconVis = false;
            JMenuItem THIS = menuItemsListMapping.get(SETTINGS_MENU).get(3);

            @Override
            public void actionPerformed(ActionEvent e) {
                iconVis = (THIS.getIcon()==null)?(false):(true);
                instantiateNetworkDialog();
                instantiateDefaultNetworkConfig();
                if (dialogDefaultNetworkConfig.defaultNetworks.size() == 0) {
                    dialogHelper.showWarningMsg("None default networks are configured!", "Warning!");
                } else {
                    dialogNetwork.setDefaultNetworks(dialogDefaultNetworkConfig.defaultNetworks);
                    if (iconVis) {
                        DebuggingHelper.Debugln("Hiding the default networks!");
                        THIS.setIcon(null);
                        THIS.setText("Show default networks");
                        dialogNetwork.showDefaultNetworks(false);
                    } else {
                        DebuggingHelper.Debugln("Showing the default networks!");
                        THIS.setIcon(new ImageIcon(icon_selected.getScaledInstance(8, 8, Image.SCALE_SMOOTH)));
                        THIS.setText("Hide default networks");
                        dialogNetwork.showDefaultNetworks(true);
                        dialogHelper.showInformationMsg("Default networks are visible!", "Success!");
                    }
                    iconVis = !iconVis;
                }
            }
        });
        this.MenusOrder.add(SETTINGS_MENU);


        this.addMenuItemsToMenus();
        this.addMenusToMenuBar();
        this.setJMenuBar(menuBar);
        // ========================================= MENU BAR CONF. ====================================================
    }

    private void setUpEventListeners()
    {
        // ========================================= ALL EVENT LISTENERS ===============================================
        this.painter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                DebuggingHelper.Debugln("Key pressed : "+e.getKeyCode());
                if (e.getKeyCode() == 32 && toolStatus==ToolStatus.TOOL_LINK_CSMA) {
                    // render the CSMA link...
                    painter.addAndPrintLink(new CsmaLinkPainter(nodesInCsma,painter.getNodes()));
                    // instantiate connection dialog...
                    instantiateConnectionDialog();
                    // get to the device dialog and...
                    dialogConnection.showDialog(dialogLink.getAllLinks(),dialogNetwork.getAllNetworks(),nodesInCsma,toolStatus);
                    // empty the nodes list...
                    nodesInCsma = new ArrayList<>();
                    // empty the reference nodes list...
                    painter.setReferenceNodes(new ArrayList<>());
                }
            }
        });

        // record each mouse click on canvas...
        // do accordingly...
        this.painter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DebuggingHelper.Debugln("Clicked at x : "+e.getX()+" y : "+e.getY());
                switch (toolStatus) {
                    // if tool is 'node' tool then...
                    case TOOL_NODE : {
                        // create a new node at given point...
                        NodePainter node = new NodePainter(e.getX()-10, e.getY()-10, 20,String.valueOf(painter.getNodes().size()));
                        // add that node to painter for painting on canvas...
                        painter.addAndPrintNode(node);
                    } break;

                    // if tool is 'link' tool then...
                    case TOOL_LINK : {
                        int collision = -1;
                        boolean successfulClick = false;
                        // for first successful click....
                        // check if collision with any node...
                        collision = painter.pointCollideWithAny(e.getX(), e.getY());
                        DebuggingHelper.Debugln("Collision : "+collision+" Clicks : "+clicks);
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
                            painter.addAndPrintLink(new P2pLinkPainter(painter.getNodes().get(firstNode), painter.getNodes().get(collision)));
                            successfulClick = true;
                            DebuggingHelper.Debugln(firstNode+" "+collision);
                            instantiateConnectionDialog();
                            ArrayList<Integer> nodes = new ArrayList<>();
                            nodes.add(firstNode);
                            nodes.add(collision);
                            dialogConnection.showDialog(dialogLink.getAllLinks(),dialogNetwork.getAllNetworks(),nodes, toolStatus);
                            firstNode = -1;
                        }
                        incrementClicks(successfulClick);
                    } break;

                    case TOOL_LINK_CSMA: {
                       int collision = -1;

                       collision = painter.pointCollideWithAny(e.getX(), e.getY());
                       DebuggingHelper.Debugln("Collision : "+collision);
                       if (collision >= 0 && !nodesInCsma.contains(collision)) {
                            nodesInCsma.add(collision);
                            painter.addAndPrintRefNode(new NodePainter(painter.getNodes().get(collision).xPos-5, painter.getNodes().get(collision).yPos-5,30,"",Color.GREEN));
                       }
                    } break;

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
                if (checkIfLinkCanBeAdded(ToolStatus.TOOL_LINK)) {
                    lbl_info.setText("Connection Tool Selected: To create a link, click on two nodes sequentially.");
                    toolStatus = ToolStatus.TOOL_LINK;
                }
            }
        });

        // action to be performed when csma link tool is selected...
        btn_tool_csmaConn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkIfLinkCanBeAdded(ToolStatus.TOOL_LINK_CSMA)) {
                    lbl_info.setText("CSMA Connection Tool Selected : To create a link, click on nodes then click spacebar!");
                    toolStatus = ToolStatus.TOOL_LINK_CSMA;
                }
            }
        });

        // event that will happen immediately when clicking on view tool
        btn_tool_view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkIfNodesExists("WARNING : No nodes to view!!")) {
                    lbl_info.setText("View Tool Selected : Double click to disable! OR Click on any node to view connections!");
                    int server_index = Integer.MIN_VALUE,client_index = Integer.MIN_VALUE;
                    if (dialogConfigureServer != null) {
                        if (dialogConfigureServer.settings.size() != 0) {
                            server_index = Integer.parseInt(dialogConfigureServer.settings.get(0));
                        }
                    }

                    if (dialogConfigureClient != null) {
                        if (dialogConfigureClient.settings.size() != 0) {
                            client_index = Integer.parseInt(dialogConfigureClient.settings.get(0));
                        }
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
                if (dialogLink != null) { // if dialog is already instantiated...
                    dialogLink.setVisible(true);
                } else { // first time instantiation....
                    instantiateLinkDialog();
                    dialogLink.setVisible(true);
                }
            }
        });

        // action to be performed when Add Network button is pressed...
        btn_addNetwork.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // logic is same as mentioned in btn_addLink's action listener...
                if (dialogNetwork != null) {
                    dialogNetwork.setVisible(true);
                } else {
                    instantiateNetworkDialog();
                    dialogNetwork.setVisible(true);
                }
            }
        });

        // action to be performed when clicking on configure server button...
        btn_serverConfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkIfNodesExists("Please add some nodes first!")) {
                   if (dialogConfigureServer != null) {
                       dialogConfigureServer.showDialog(painter.getNodes().size());
                   } else {
                       Map<String, JComponent> helpfulComponents = new HashMap<>();
                       helpfulComponents.put(Dialog_ConfigureServer.COMPONENT_OVERVIEW_LABEL, lbl_server);
                       dialogConfigureServer = new Dialog_ConfigureServer(painter.getNodes().size(), helpfulComponents);
                       dialogConfigureServer.showDialog(painter.getNodes().size());
                   }
                }
            }
        });

        // action to be performed when clicking on configure client button...
        btn_clientConfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkIfNodesExists("Please add some nodes first!")) {
                    if (dialogConfigureServer.settings.size() == 0) { // server configuration has not been done yet...
                        // show warning message...
                        dialogHelper.showWarningMsg("Please configure the server first!", "Warning!");
                    } else { // otherwise...
                        if (dialogConfigureClient != null) {
                            // show client configuration dialog box...
                            dialogConfigureClient.showDialog(painter.getNodes().size());
                        } else {
                            Map<String, JComponent> helpfulComponents = new HashMap<>();
                            helpfulComponents.put(Dialog_ConfigureClient.COMPONENT_OVERVIEW_LABEL, lbl_client);
                            dialogConfigureClient = new Dialog_ConfigureClient(painter.getNodes().size(), helpfulComponents);
                            dialogConfigureClient.showDialog(painter.getNodes().size());
                        }
                    }
                }
            }
        });

        // action to be performed when clicking on generate code button...
        btn_generateCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkIfNodesExists("Please create your topology first!")) {
                    createFile();
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

        // save settings while main window is being closed!!
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                // pre-processing before saving the settings...
                if (dialogDefaultLinkConfig != null) { // if there are some default links to be configured....
                    if (dialogDefaultLinkConfig.defaultLinks.size() > 0) {
                        if (UNIVERSAL_SETTINGS.has(DEFAULT_LINKS)) {
                            UNIVERSAL_SETTINGS.remove(DEFAULT_LINKS);
                        }
                        ArrayList<String> links = new ArrayList<>();
                        for (NetworkLink link : dialogDefaultLinkConfig.defaultLinks) {
                            links.add(link.forSettings());
                        }
                        UNIVERSAL_SETTINGS.put(DEFAULT_LINKS, links);
                    }
                }

                if (dialogDefaultNetworkConfig != null) { // if there are some default networks to be configured...
                    if (dialogDefaultNetworkConfig.defaultNetworks.size() > 0) {
                        if (UNIVERSAL_SETTINGS.has(DEFAULT_NETWORKS)) {
                            UNIVERSAL_SETTINGS.remove(DEFAULT_NETWORKS);
                        }
                        ArrayList<String> networks = new ArrayList<>();
                        for (Network network : dialogDefaultNetworkConfig.defaultNetworks) {
                            networks.add(network.forSettings());
                        }
                        UNIVERSAL_SETTINGS.put(DEFAULT_NETWORKS, networks);
                    }
                }

                // saving the settings...
                saveSettings();
            }
        });
        // ==================================== ALL EVENT LISTENERS ENDS ===============================================
    }

    private void instantiateConnectionDialog() {
        if (dialogConnection == null) {
            dialogConnection = new Dialog_Connection();
        }
    }

    private void instantiateLinkDialog() {
        if (dialogLink == null) {
            Map<String, JComponent> helpfulComponents = new HashMap<>();
            helpfulComponents.put(Dialog_Link.COMPONENT_COMBO_BOX, comboBox_links);
            helpfulComponents.put(Dialog_Link.COMPONENT_OVERVIEW_LABEL, lbl_links);
            dialogLink = new Dialog_Link(helpfulComponents);
        }
    }

    private void instantiateNetworkDialog() {
        if (dialogNetwork == null) {
            Map<String, JComponent> helpfulComponents = new HashMap<>();
            helpfulComponents.put(Dialog_Network.COMPONENT_COMBO_BOX, comboBox_networks);
            helpfulComponents.put(Dialog_Network.COMPONENT_OVERVIEW_LABEL, lbl_networks);
            dialogNetwork = new Dialog_Network(helpfulComponents);
        }
    }

    private void instantiateDefaultLinkConfig() {
        if (dialogDefaultLinkConfig == null) {
            dialogDefaultLinkConfig = new Dialog_DefaultLinkConfig(new ArrayList<>());
        }
        if (hasDefaultLinks()) {
            dialogDefaultLinkConfig.defaultLinks = getDefaultLinks();
        }
    }

    private void instantiateDefaultNetworkConfig() {
        if (dialogDefaultNetworkConfig == null) {
            dialogDefaultNetworkConfig = new Dialog_DefaultNetworkConfig(new ArrayList<>());
        }
        if (hasDefaultNetworks()) {
            dialogDefaultNetworkConfig.defaultNetworks = getDefaultNetworks();
        }
    }

    private void createFile() {
        if (this.lbl_server.getText().equalsIgnoreCase("Server Index :  Not configured")) {
            if (dialogHelper.showConfirmationDialog("There are no servers in the topology!\nDo you want to continue?","Warning!") == JOptionPane.NO_OPTION) {
                return;
            }
        }

        if (this.lbl_client.getText().equalsIgnoreCase("Client Index  : Not configured")) {
            if (dialogHelper.showConfirmationDialog("There are no clients in the topology!","Warning!")==JOptionPane.NO_OPTION) {
                return;
            }
        }

        String path = UNIVERSAL_SETTINGS.getString(OUTPUT_PATH)+"\\"+UNIVERSAL_SETTINGS.getString(FILE_NAME)+".txt";

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
        this.codeGenerator = new CodeGenerator(dialogConfigureServer,dialogConfigureClient,dialogConnection,dialogLink,otherFields);
        this.codeGenerator.GenerateCode();
        FileReaderWriter.writeUsingPath(this.codeGenerator.getCode(), path);

        this.dialogHelper.showInformationMsg("File has been generated successfully!\nAt : "+this.OutputPath, "Code Generated!");
    }

    public static void main(String[] args) {
        new Home_Screen();
    }
}
