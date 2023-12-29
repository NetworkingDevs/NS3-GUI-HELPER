import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home_Screen extends JFrame {
    private final String FILE_MENU = "File";
    private final String SCENARIOS_MENU = "Scenarios";
    private final String SETTINGS_MENU = "Settings";
    private ArrayList<String> MenusOrder = new ArrayList<>();

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

    public Home_Screen() {
        // basic initialization of this component...
        // ========================================= BASIC CONF. =======================================================
        this.setContentPane(this.JPanel_main);
        this.setTitle("Topology Helper - NS3");
        this.setSize(850,600);
        this.setMaximumSize(new Dimension(1200,1000));
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(800,600));
        this.menuMapping = new HashMap<>();
        this.menuItemsListMapping = new HashMap<>();
        // ========================================= BASIC CONF. =======================================================

        // Adding the menu bar to this component...
        // ========================================= MENU BAR CONF. ====================================================
        this.menuBar = new JMenuBar();

        // adding each menu for menu bar...

        // step-1 : Add a menu in menus
        this.menuMapping.put(FILE_MENU,new JMenu("File"));
        // step-2 : create a menu item list
        this.menuItemsListMapping.put(FILE_MENU,new ArrayList<>());
        // step-3 : add menu items
        this.menuItemsListMapping.get(FILE_MENU).add(new JMenuItem("Output Path"));
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

    public static void main(String[] args) {
        new Home_Screen();
    }
}
