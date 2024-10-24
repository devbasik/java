import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class PizzaBillGenerator extends JFrame implements ActionListener {

    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);

    JTextField customerNameField;
    JComboBox<String> pizzaTypeCombo, pizzaSizeCombo;
    JCheckBox cheese, oregano, chilliFlakes, veggies, pepperoni;

    boolean cocaColaSelected, pepsiSelected, spriteSelected;
    boolean ketchupSelected, mayoSelected, cheeseSauceSelected;

    JTextArea billArea;

    double basePrice = 0.0, toppingsPrice = 0.0, sidesPrice = 0.0, drinksPrice = 0.0;

    public PizzaBillGenerator() {
        setTitle("Pizza Bill Generator");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setupPanels();
        add(mainPanel);
        setVisible(true);
    }

    private void setupPanels() {
        // Customer Name Panel
        JPanel customerNamePanel = new JPanel(new GridLayout(3, 1));
        customerNameField = new JTextField();
        JButton nextButton0 = new JButton("Next");
        nextButton0.addActionListener(this);
        customerNamePanel.add(new JLabel("Enter Customer's Name:"));
        customerNamePanel.add(customerNameField);
        customerNamePanel.add(nextButton0);

        // Pizza Type Panel
        JPanel pizzaTypePanel = new JPanel(new GridLayout(3, 1));
        String[] pizzaTypes = {"Margherita ($8)", "Veggie ($10)", "Chicken ($12)"};
        pizzaTypeCombo = new JComboBox<>(pizzaTypes);
        JButton nextButton1 = new JButton("Next");
        nextButton1.addActionListener(this);
        pizzaTypePanel.add(new JLabel("Select Pizza Type:"));
        pizzaTypePanel.add(pizzaTypeCombo);
        pizzaTypePanel.add(nextButton1);

        // Pizza Size Panel
        JPanel pizzaSizePanel = new JPanel(new GridLayout(3, 1));
        String[] pizzaSizes = {"Regular", "Medium (+$2)", "Large (+$4)"};
        pizzaSizeCombo = new JComboBox<>(pizzaSizes);
        JButton nextButton2 = new JButton("Next");
        nextButton2.addActionListener(this);
        pizzaSizePanel.add(new JLabel("Select Pizza Size:"));
        pizzaSizePanel.add(pizzaSizeCombo);
        pizzaSizePanel.add(nextButton2);

        // Toppings Panel
        JPanel toppingsPanel = new JPanel(new GridLayout(7, 1));
        cheese = new JCheckBox("Cheese ($1)");
        oregano = new JCheckBox("Oregano ($0.50)");
        chilliFlakes = new JCheckBox("Chilli Flakes ($0.50)");
        veggies = new JCheckBox("Veggies ($1.50)");
        pepperoni = new JCheckBox("Pepperoni ($2)");
        JButton addDrinksButton = new JButton("Add Drinks & Sides");
        addDrinksButton.addActionListener(this);
        JButton nextButton3 = new JButton("Next");
        nextButton3.addActionListener(this);
        toppingsPanel.add(new JLabel("Select Toppings:"));
        toppingsPanel.add(cheese);
        toppingsPanel.add(oregano);
        toppingsPanel.add(chilliFlakes);
        toppingsPanel.add(veggies);
        toppingsPanel.add(pepperoni);
        toppingsPanel.add(addDrinksButton);
        toppingsPanel.add(nextButton3);

        // Bill Panel
        JPanel billPanel = new JPanel(new BorderLayout());
        billArea = new JTextArea();
        billArea.setEditable(false);
        billArea.setBackground(Color.BLACK);
        billArea.setForeground(Color.WHITE);
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(billArea);
        JButton finishButton = new JButton("Finish");
        finishButton.addActionListener(this);
        billPanel.add(scrollPane, BorderLayout.CENTER);
        billPanel.add(finishButton, BorderLayout.SOUTH);

        mainPanel.add(customerNamePanel, "CustomerName");
        mainPanel.add(pizzaTypePanel, "PizzaType");
        mainPanel.add(pizzaSizePanel, "PizzaSize");
        mainPanel.add(toppingsPanel, "Toppings");
        mainPanel.add(billPanel, "Bill");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        if (action.equals("Add Drinks & Sides")) {
            openDialog();
        } else if (action.equals("Next")) {
            cardLayout.next(mainPanel);
        } else if (action.equals("Finish")) {
            updateBill();
            resetSelections();
            JOptionPane.showMessageDialog(this, "Order Completed! Taking a new order.");
            cardLayout.show(mainPanel, "CustomerName");
        }
    }

    private void openDialog() {
        JDialog dialog = new JDialog(this, "Select Drinks & Sides", true);
        dialog.setSize(300, 300);
        dialog.setLayout(new GridLayout(7, 1));

        JCheckBox cocaCola = new JCheckBox("Coca Cola ($2)");
        JCheckBox pepsi = new JCheckBox("Pepsi ($2)");
        JCheckBox sprite = new JCheckBox("Sprite ($2)");
        JCheckBox ketchup = new JCheckBox("Ketchup");
        JCheckBox mayo = new JCheckBox("Mayonnaise ($0.25)");
        JCheckBox cheeseSauce = new JCheckBox("Cheese Sauce ($0.50)");

        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(e -> {
            cocaColaSelected = cocaCola.isSelected();
            pepsiSelected = pepsi.isSelected();
            spriteSelected = sprite.isSelected();
            ketchupSelected = ketchup.isSelected();
            mayoSelected = mayo.isSelected();
            cheeseSauceSelected = cheeseSauce.isSelected();
            dialog.dispose();
        });

        dialog.add(cocaCola);
        dialog.add(pepsi);
        dialog.add(sprite);
        dialog.add(ketchup);
        dialog.add(mayo);
        dialog.add(cheeseSauce);
        dialog.add(doneButton);

        dialog.setVisible(true);
    }

    private void updateBill() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        calculatePrices();

        StringBuilder bill = new StringBuilder();
        bill.append("+----------------------------------+\n");
        bill.append("|            Pizza Bill            |\n");
        bill.append("+----------------------------------+\n");
        bill.append("Customer: ").append(customerNameField.getText()).append("\n");
        bill.append("Date: ").append(now.format(formatter)).append("\n\n");
        bill.append("Pizza: ").append(pizzaTypeCombo.getSelectedItem()).append("\n");
        bill.append("Size: ").append(pizzaSizeCombo.getSelectedItem()).append("\n");
        bill.append("Toppings:\n");

        if (cheese.isSelected()) bill.append("  - Cheese ($1)\n");
        if (oregano.isSelected()) bill.append("  - Oregano ($0.50)\n");
        if (chilliFlakes.isSelected()) bill.append("  - Chilli Flakes ($0.50)\n");
        if (veggies.isSelected()) bill.append("  - Veggies ($1.50)\n");
        if (pepperoni.isSelected()) bill.append("  - Pepperoni ($2)\n");

        bill.append("Drinks & Sides:\n");
        if (cocaColaSelected) bill.append("  - Coca Cola ($2)\n");
        if (pepsiSelected) bill.append("  - Pepsi ($2)\n");
        if (spriteSelected) bill.append("  - Sprite ($2)\n");
        if (mayoSelected) bill.append("  - Mayonnaise ($0.25)\n");
        if (cheeseSauceSelected) bill.append("  - Cheese Sauce ($0.50)\n");

        double total = basePrice + toppingsPrice + sidesPrice + drinksPrice;
        bill.append("\nTotal: $").append(String.format("%.2f", total)).append("\n");

        billArea.setText(bill.toString());
        cardLayout.show(mainPanel, "Bill");
    }

    private void calculatePrices() {
        basePrice = switch ((String) pizzaTypeCombo.getSelectedItem()) {
            case "Margherita ($8)" -> 8.0;
            case "Veggie ($10)" -> 10.0;
            case "Chicken ($12)" -> 12.0;
            default -> 0.0;
        };

        basePrice += switch ((String) pizzaSizeCombo.getSelectedItem()) {
            case "Medium (+$2)" -> 2.0;
            case "Large (+$4)" -> 4.0;
            default -> 0.0;
        };

        toppingsPrice = (cheese.isSelected() ? 1.0 : 0.0) +
                        (oregano.isSelected() ? 0.5 : 0.0) +
                        (chilliFlakes.isSelected() ? 0.5 : 0.0) +
                        (veggies.isSelected() ? 1.5 : 0.0) +
                        (pepperoni.isSelected() ? 2.0 : 0.0);

        drinksPrice = (cocaColaSelected ? 2.0 : 0.0) +
                      (pepsiSelected ? 2.0 : 0.0) +
                      (spriteSelected ? 2.0 : 0.0);

        sidesPrice = (mayoSelected ? 0.25 : 0.0) +
                     (cheeseSauceSelected ? 0.50 : 0.0);
    }

    private void resetSelections() {
        customerNameField.setText("");
        pizzaTypeCombo.setSelectedIndex(0);
        pizzaSizeCombo.setSelectedIndex(0);
        cheese.setSelected(false);
        oregano.setSelected(false);
        chilliFlakes.setSelected(false);
        veggies.setSelected(false);
        pepperoni.setSelected(false);

        cocaColaSelected = pepsiSelected = spriteSelected = false;
        ketchupSelected = mayoSelected = cheeseSauceSelected = false;
    }

    public static void main(String[] args) {
        new PizzaBillGenerator();
    }
}
