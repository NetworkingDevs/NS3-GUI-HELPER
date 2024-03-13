import Dialogs.*;
import FileHandler.CodeGenerator;
import FileHandler.FileReaderWriter;
import GuiRenderers.*;
import Helpers.LoggingHelper;
import StatusHelper.LogLevel;
import StatusHelper.ToolStatus;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static Helpers.ApplicationSettingsHelper.*;

/**
 * This is the main entry class for the application, This will centrally responsible for
 * all Dialog boxes management, Code Generation and Canvas renderings.
 * */
public class Home_Screen extends JFrame {
    /**
     * a constant string to track File menu's key
     * */
    private final String FILE_MENU = "File";
    /**
     * a constant string to track key of Settings menu
     * */
    private final String SETTINGS_MENU = "Settings";
    /**
     * a constant string to track key of Help menu
     * */
    private final String HELP_MENU = "Help";
    /**
     * an array list tp track the menu names (String) as key for menu bar
     * */
    private ArrayList<String> MenusOrder = new ArrayList<>();
    /**
     * minimum window width
     * */
    private final int MIN_WINDOW_WIDTH = 1090;
    /**
     * minimum window height
     * */
    private final int MIN_WINDOW_HEIGHT = 650;

    // UI Elements
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
    private JButton btn_tool_wifiConn;
    private JPanel JPanel_grp_wifiLinks;
    private JLabel lbl_wifi;
    private JButton btn_addWiFiLink;
    private JComboBox comboBox_wifiLinks;
    private JLabel lbl_wifi_links;
    /**
     * menu bar for the application
     * */
    JMenuBar menuBar; // this is a menu bar
    /**
     * mapping of string keys to menu
     * */
    Map<String, JMenu> menuMapping; // this is a mapping (from string to each menu), for ease in coding...
    /**
     * mapping of string keys to menu items for each menu
     * */
    Map<String, ArrayList<JMenuItem>> menuItemsListMapping; // this is a mapping (from string to list of each menu's item's list)
    /**
     * to keep track of selected nodes in CSMA channel
     * */
    ArrayList<Integer> nodesSelected = new ArrayList<>();

    /**
     * Images for the icons and other settings
     * */
    Image imgInfo, icon_nodeTool, icon_p2pLinkTool, icon_csmaLinkTool, icon_viewTool, icon_selected, icon_wifiLinkTool;

    {
        try {
            imgInfo = ImageIO.read(getClass().getClassLoader().getResource("info.png"));
            icon_nodeTool = ImageIO.read(getClass().getClassLoader().getResource("icon_tool_node.png"));
            icon_p2pLinkTool = ImageIO.read(getClass().getClassLoader().getResource("icon_tool_p2pLink.png"));
            icon_csmaLinkTool = ImageIO.read(getClass().getClassLoader().getResource("icon_tool_csmaLink.png"));
            icon_viewTool = ImageIO.read(getClass().getClassLoader().getResource("icon_tool_view.png"));
            icon_selected = ImageIO.read(getClass().getClassLoader().getResource("icon_selected.png"));
            icon_wifiLinkTool = ImageIO.read(getClass().getClassLoader().getResource("icon_tool_wifiLink.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // creating the variables for serving functionalities ==============================================================
    /**
     * a painter to draw elements and objects on the canvas
     * */
    TopologyPainter painter = new TopologyPainter(new ArrayList<>(), new ArrayList<>(), 800, 800);
    /**
     * to keep the track of the selected tool
     * */
    ToolStatus toolStatus;
    /**
     * to keep the track of the no. of clicks
     * */
    private int clicks = 1;
    /**
     * to keep the track of the index of the first selected node
     * */
    private int firstNode = -1;
    /**
     * to manage links created by the user
     * */
    Dialog_Link dialogLink;
    /**
     * to manage wi-fi links created by the user
     * */
    Dialog_WiFiLink dialogWiFiLink;
    /**
     * to manage the network settings created by the user
     * */
    Dialog_Network dialogNetwork;
    /**
     * to make a connection (wired, common bus) between two nodes
     * */
    Dialog_Connection dialogConnection;
    /**
     * to configure a single UDP Echo Server
     * */
    Dialog_ConfigureServer dialogConfigureServer;
    /**
     * to configure a single UDP Echo client
     * */
    Dialog_ConfigureClient dialogConfigureClient;
    /**
     * to manage the settings for output file that will be generated
     * */
    Dialog_outputFileChooser dialogOutputFileChooser;
    /**
     * to show the alerts, information, warning and confirmation messages
     * */
    Dialog_Helper dialogHelper;
    /**
     * to manage the default links
     * */
    Dialog_DefaultLinkConfig dialogDefaultLinkConfig;
    /**
     * to manage the default network settings
     * */
    Dialog_DefaultNetworkConfig dialogDefaultNetworkConfig;
    /**
     * for selecting the outPut path
     * */
    String OutputPath;
    /**
     * for generating the equivalent NS-3 Script
     * */
    CodeGenerator codeGenerator;

    /**
     * constructor that initializes the necessary objects, events, etc.
     * */
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
        this.btn_tool_wifiConn.setIcon(new ImageIcon(icon_wifiLinkTool.getScaledInstance(50,50, Image.SCALE_AREA_AVERAGING)));
        this.btn_tool_wifiConn.setText("");
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

    /**
     * to add each menu to menu bar list
     *
     * @since 1.0.0
     * */
    private void addMenusToMenuBar() {
        for (String menu : this.MenusOrder) { // adding each menu to menu bar...
            this.menuBar.add(this.menuMapping.get(menu));
        }
    }

    /**
     * to add each menu item to the menu
     *
     * @since 1.0.0
     * */
    private void addMenuItemsToMenus() {
        for (Map.Entry<String, JMenu> menu : this.menuMapping.entrySet()) { // for each menu...
            for (JMenuItem item : this.menuItemsListMapping.get(menu.getKey())) { // adding each menu item...
                menu.getValue().add(item);
            }
        }
    }

    /**
     * to increment click if, clicked on rendered node
     *
     * @param successfulClick whether clicked on any of rendered node
     * @since 0.3.0
     * */
    private void incrementClicks(boolean successfulClick) {
        if (successfulClick) {
            clicks ++;
        }
    }

    /**
     * to check if, user is allowed to add new link
     *
     * @param toolStatus the current selected tool
     * @return boolean variable showing, whether the user is allowed to add new link
     * @since 0.3.0
     * */
    private boolean checkIfLinkCanBeAdded(ToolStatus toolStatus) {
        instantiateLinkDialog();
        instantiateWifiLinkDialog();
        LoggingHelper.LogDebug(dialogLink.getP2pLinkCount()+" CSMA Link Count : "+dialogLink.getCsmaLinkCount());
        // link tool selection is invalid in below conditions...
        if (this.painter.getNodes().size() == 0) { // if there are no node...
            this.dialogHelper.showWarningMsg("Please add some nodes to make a connection!", "Warning");
            return false;
        } else if ((toolStatus == ToolStatus.TOOL_LINK)?(dialogLink.getP2pLinkCount()==0):((toolStatus == ToolStatus.TOOL_LINK_CSMA)?(dialogLink.getCsmaLinkCount()==0):(((toolStatus == ToolStatus.TOOL_LINK_WIFI)?(dialogWiFiLink.getLinkCount() == 0):(false))))) { // if there are no links...
            this.dialogHelper.showWarningMsg("Please create at least one link before making a connection!", "Warning");
            return false;
        } else if (dialogNetwork==null || (dialogNetwork.getNetworkCount() == 0)) { // if there are no network settings made...
            this.dialogHelper.showWarningMsg("Please create at least one network before making a connection!", "Warning");
            return false;
        } else {
            return true;
        }
    }

    /**
     * it checks that whether there is any node rendered on the canvas
     *
     * @param msg The error message to show, in case no nodes are rendered
     * @return boolean variable showcasing that whether there are any nodes on the canvas
     * @since 0.3.0
     * */
    private boolean checkIfNodesExists (String msg) {
        if (painter.getNodes().size() == 0) {
            this.dialogHelper.showErrorMsg(msg, "Error!");
            return false;
        }
        return true;
    }

    /**
     * to open a web page from the URI
     *
     * @param uri The URI of the webpage
     * @return boolean variable showing that the URI exists or not
     * @since 1.1.0
     * */
    private static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * to open a web page from the URL
     *
     * @param url The URL of the file
     * @return boolean variable showing that the URL exist or not
     * @see Home_Screen#openWebpage(URI)
     * @since 1.1.0
     * */
    private static boolean openWebpage(URL url) {
        try {
            return openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * it will set up the menu bar for the application
     *
     * @see Home_Screen#addMenuItemsToMenus()
     * @see Home_Screen#addMenusToMenuBar()
     * @since 1.0.0
     * */
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
                            LoggingHelper.LogDebug("Output settings has been altered!");
                            UNIVERSAL_SETTINGS.remove(OUTPUT_PATH);
                            UNIVERSAL_SETTINGS.put(OUTPUT_PATH, dialogOutputFileChooser.getOutputPath());
                            UNIVERSAL_SETTINGS.remove(FILE_NAME);
                            UNIVERSAL_SETTINGS.put(FILE_NAME, dialogOutputFileChooser.getFileName());
                            LoggingHelper.LogDebug("Updated JSON : " + UNIVERSAL_SETTINGS.toString());
                        }
                    });
                    dialogOutputFileChooser.setVisible(true);
                }
            }
        });
        // step-4 : add menu to menu order
        this.MenusOrder.add(FILE_MENU);
        // following above 4 steps for each menu...

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
            boolean iconVis = menuItemsListMapping.get(SETTINGS_MENU).get(1).getIcon()!=null;
            JMenuItem THIS = menuItemsListMapping.get(SETTINGS_MENU).get(1);

            @Override
            public void actionPerformed(ActionEvent e) {
                iconVis = (THIS.getIcon()!=null);
                instantiateDefaultLinkConfig();
                instantiateLinkDialog();
                if (dialogDefaultLinkConfig.defaultLinks.size() == 0) {
                    dialogHelper.showWarningMsg("None default links are configured!", "Warning!");
                } else {
                    dialogLink.setDefaultLinks(dialogDefaultLinkConfig.defaultLinks);
                    if (iconVis) {
                        LoggingHelper.LogDebug("Hiding the default links!");
                        THIS.setIcon(null);
                        THIS.setText("Show default links");
                        dialogLink.showDefaultLinks(false);
                    } else {
                        LoggingHelper.LogDebug("Showing the default links!");
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
                        LoggingHelper.LogDebug("Hiding the default networks!");
                        THIS.setIcon(null);
                        THIS.setText("Show default networks");
                        dialogNetwork.showDefaultNetworks(false);
                    } else {
                        LoggingHelper.LogDebug("Showing the default networks!");
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

        this.menuMapping.put(HELP_MENU, new JMenu("Help"));
        this.menuItemsListMapping.put(HELP_MENU, new ArrayList<>());
        this.menuItemsListMapping.get(HELP_MENU).add(new JMenuItem("User Manual"));
        this.menuItemsListMapping.get(HELP_MENU).get(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    openWebpage(new URL("https://networkingdevs.github.io/NS3-GUI-HELPER/manual.html"));
                } catch (MalformedURLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        this.MenusOrder.add(HELP_MENU);


        this.addMenuItemsToMenus();
        this.addMenusToMenuBar();
        this.setJMenuBar(menuBar);
        // ========================================= MENU BAR CONF. ====================================================
    }

    /**
     * to set up all events listeners
     *
     * @since 0.3.0
     * */
    private void setUpEventListeners()
    {
        // ========================================= ALL EVENT LISTENERS ===============================================
        this.painter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                LoggingHelper.LogDebug("Key pressed : "+e.getKeyCode());
                if (e.getKeyCode() == 32) {
                    switch (toolStatus) {
                        case TOOL_LINK_CSMA : {
                            // render the CSMA link...
                            painter.addAndPrintLink(new CsmaLinkPainter(nodesSelected,painter.getNodes()));
                            // instantiate connection dialog...
                            instantiateConnectionDialog();
                            // get to the device dialog and...
                            dialogConnection.showDialog(dialogLink.getAllLinks(),dialogNetwork.getAllNetworks(), nodesSelected,toolStatus);
                            // empty the nodes list...
                            nodesSelected = new ArrayList<>();
                            // empty the reference nodes list...
                            painter.setReferenceNodes(new ArrayList<>());
                        } break;

                        case TOOL_LINK_WIFI : {
                            // render the wi-fi link
                            painter.addAndPrintLink(new WifiLinkPainter(nodesSelected,painter.getNodes()));
                            // instantiate connection dialog
                            instantiateConnectionDialog();
                            // get the device dialog
                            dialogConnection.showDialog(dialogWiFiLink.getAllLinks(),dialogNetwork.getAllNetworks(), nodesSelected,toolStatus);
                            // empty the nodes list
                            nodesSelected = new ArrayList<>();
                            // empty the reference node list
                            painter.setReferenceNodes(new ArrayList<>());
                        }
                    }
                }
            }
        });

        // record each mouse click on canvas...
        // do accordingly...
        this.painter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                LoggingHelper.LogDebug("Clicked at x : "+e.getX()+" y : "+e.getY());
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
                        LoggingHelper.LogDebug("Collision : "+collision+" Clicks : "+clicks);
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
                            LoggingHelper.LogDebug(firstNode+" "+collision);
                            instantiateConnectionDialog();
                            ArrayList<Integer> nodes = new ArrayList<>();
                            nodes.add(firstNode);
                            nodes.add(collision);
                            dialogConnection.showDialog(dialogLink.getAllLinks(),dialogNetwork.getAllNetworks(),nodes, toolStatus);
                            firstNode = -1;
                        }
                        incrementClicks(successfulClick);
                    } break;

                    case TOOL_LINK_CSMA, TOOL_LINK_WIFI: {
                       int collision = -1;

                       collision = painter.pointCollideWithAny(e.getX(), e.getY());
                       LoggingHelper.LogDebug("Collision : "+collision);
                       if (collision >= 0 && !nodesSelected.contains(collision)) {
                            nodesSelected.add(collision);
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

        btn_tool_wifiConn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (checkIfLinkCanBeAdded(ToolStatus.TOOL_LINK_WIFI)) {
                    lbl_info.setText("Wi-Fi Connection Tool : Select the AP node first, then select the remaining nodes, and press the spacebar.");
                    toolStatus = ToolStatus.TOOL_LINK_WIFI;
                }
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

        // action to be performed when add Wi-Fi link button is clicked.
        btn_addWiFiLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dialogWiFiLink != null) {
                    dialogWiFiLink.setVisible(true);
                } else {
                    instantiateWifiLinkDialog();
                    dialogWiFiLink.setVisible(true);
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
                instantiateDefaultLinkConfig();
                saveDefaultLinks(dialogDefaultLinkConfig.defaultLinks);

                instantiateDefaultNetworkConfig();
                saveDefaultNetworks(dialogDefaultNetworkConfig.defaultNetworks);

                // saving the settings...
                saveSettings();
            }
        });
        // ==================================== ALL EVENT LISTENERS ENDS ===============================================
    }

    /**
     * to instantiate the object for connection dialog
     *
     * @since 1.0.0
     * */
    private void instantiateConnectionDialog() {
        dialogConnection = Dialog_Connection.getInstance();
        instantiateLinkDialog();
        dialogConnection.addDialogLink(dialogLink);
        dialogConnection.addDialogLink(dialogWiFiLink);
    }

    /**
     * to instantiate the object for link dialog
     *
     * @since 1.0.0
     * */
    private void instantiateLinkDialog() {
        Map<String, JComponent> helpfulComponents = new HashMap<>();
        helpfulComponents.put(Dialog_Link.COMPONENT_COMBO_BOX, comboBox_links);
        helpfulComponents.put(Dialog_Link.COMPONENT_OVERVIEW_LABEL, lbl_links);
        dialogLink = Dialog_Link.getInstance(helpfulComponents);
    }

    /**
     * to instantiate the object for wi-fi link dialog
     *
     * @since 1.2.0
     * */
    private void instantiateWifiLinkDialog() {
        Map<String, JComponent> helpfulComponents = new HashMap<>();
        helpfulComponents.put(Dialog_WiFiLink.COMPONENT_COMBO_BOX,comboBox_wifiLinks);
        helpfulComponents.put(Dialog_WiFiLink.COMPONENT_OVERVIEW_LABEL, lbl_wifi_links);
        dialogWiFiLink = Dialog_WiFiLink.getInstance(helpfulComponents);
    }

    /**
     * to instantiate the object for network dialog
     *
     * @since 1.0.0
     * */
    private void instantiateNetworkDialog() {
        Map<String, JComponent> helpfulComponents = new HashMap<>();
        helpfulComponents.put(Dialog_Network.COMPONENT_COMBO_BOX, comboBox_networks);
        helpfulComponents.put(Dialog_Network.COMPONENT_OVERVIEW_LABEL, lbl_networks);
        dialogNetwork = Dialog_Network.getInstance(helpfulComponents);
    }

    /**
     * to instantiate the object for default link dialog
     *
     * @since 1.0.0
     * */
    private void instantiateDefaultLinkConfig() {
        dialogDefaultLinkConfig = Dialog_DefaultLinkConfig.getInstance(new ArrayList<>());
        if (hasDefaultLinks()) {
            dialogDefaultLinkConfig.defaultLinks = getDefaultLinks();
        }
    }

    /**
     * to instantiate the object for default network config dialog
     *
     * @since 1.0.0
     * */
    private void instantiateDefaultNetworkConfig() {
        dialogDefaultNetworkConfig = Dialog_DefaultNetworkConfig.getInstance(new ArrayList<>());
        if (hasDefaultNetworks()) {
            dialogDefaultNetworkConfig.defaultNetworks = getDefaultNetworks();
        }
    }

    /**
     * to generate the NS-3 file for equivalent topology
     *
     * @since 0.0.0
     * */
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
        this.codeGenerator = new CodeGenerator(dialogConfigureServer,dialogConfigureClient,dialogConnection,dialogLink,dialogWiFiLink,otherFields);
        this.codeGenerator.GenerateCode();
        FileReaderWriter.writeUsingPath(this.codeGenerator.getCode(), path);

        this.dialogHelper.showInformationMsg("File has been generated successfully!\nAt : "+this.OutputPath, "Code Generated!");
    }

    /**
     * The entry point for the application
     * */
    public static void main(String[] args) {
        try {
            // setting the UI theme, if it exists
            for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        // enabling logger
        new LoggingHelper();
        // starting application
        new Home_Screen();
        // filtering the arguments...
        for (String a : args) {
            // enabling log level
            switch (a.trim().replace("-","").toUpperCase()) {
                case "ERROR":
                    LoggingHelper.setLogLevel(LogLevel.LOG_LEVEL_ERROR);
                    break;
                case "DEBUG":
                    LoggingHelper.setLogLevel(LogLevel.LOG_LEVEL_DEBUG);
                    break;
                case "INFO":
                    LoggingHelper.setLogLevel(LogLevel.LOG_LEVEL_INFO);
                    break;
                case "FUNCTION":
                    LoggingHelper.setLogLevel(LogLevel.LOG_LEVEL_FUNCTION);
                    break;
                case "LOGIC":
                    LoggingHelper.setLogLevel(LogLevel.LOG_LEVEL_LOGIC);
                    break;
                case "LOGALL":
                    LoggingHelper.setLogLevel(LogLevel.LOG_LEVEL_ALL);
                default:
                    LoggingHelper.setLogLevel(LogLevel.LOG_NONE);
                    break;
            }
        }
    }
}
