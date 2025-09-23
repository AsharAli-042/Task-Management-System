package startproject;

public class TestClass {
    public static void main(String[] args) {
        User userManager = new User();
        Dashboard dashboard = new Dashboard(userManager);

                javax.swing.SwingUtilities.invokeLater(() -> new Menu1GUI(userManager, dashboard));
            }
        }


