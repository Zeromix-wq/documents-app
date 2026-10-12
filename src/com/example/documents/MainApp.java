package com.example.documents;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends JFrame {
    private DocumentManager manager = new DocumentManager();
    private JPanel listPanel;
    private JScrollPane listScroll;
    private List<JCheckBox> checkBoxes = new ArrayList<>();
    private List<Document> documents = new ArrayList<>();

    public MainApp() {
        setTitle("Тест");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout(10, 10));

        // Левая часть — список с чекбоксами
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listScroll = new JScrollPane(listPanel);

        // Правая часть — кнопки
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(0, 1, 5, 5));

        JButton btnInvoice = new JButton("Накладная");
        JButton btnPayment = new JButton("Платёжка");
        JButton btnRequest = new JButton("Заявка на оплату");
        JButton btnSave = new JButton("Сохранить");
        JButton btnLoad = new JButton("Загрузить");
        JButton btnView = new JButton("Просмотр");
        JButton btnDelete = new JButton("Удалить выбранные");
        JButton btnExit = new JButton("Выход");

        btnInvoice.addActionListener(e -> showCreateDialog("Накладная"));
        btnPayment.addActionListener(e -> showCreateDialog("Платёжка"));
        btnRequest.addActionListener(e -> showCreateDialog("Заявка на оплату"));
        btnSave.addActionListener(e -> saveToFile());
        btnLoad.addActionListener(e -> loadFromFile());
        btnView.addActionListener(e -> viewDocument());
        btnDelete.addActionListener(e -> deleteSelected());
        btnExit.addActionListener(e -> System.exit(0));

        rightPanel.add(btnInvoice);
        rightPanel.add(btnPayment);
        rightPanel.add(btnRequest);
        rightPanel.add(btnSave);
        rightPanel.add(btnLoad);
        rightPanel.add(btnView);
        rightPanel.add(btnDelete);
        rightPanel.add(btnExit);

        add(listScroll, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private void refreshList() {
        listPanel.removeAll();
        checkBoxes.clear();
        for (Document doc : documents) {
            JCheckBox cb = new JCheckBox(doc.getDisplayText());
            cb.addActionListener(e -> {
                // можно добавить логику при клике
            });
            checkBoxes.add(cb);
            listPanel.add(cb);
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private void showCreateDialog(String type) {
        JDialog dialog = new JDialog(this, "Создание: " + type, true);
        dialog.setSize(450, 350);
        dialog.setLayout(new GridLayout(0, 2, 5, 5));

        JTextField txtNumber = new JTextField();
        JTextField txtDate = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        JTextField txtUser = new JTextField();

        dialog.add(new JLabel("Номер:"));
        dialog.add(txtNumber);
        dialog.add(new JLabel("Дата (дд.мм.гггг):"));
        dialog.add(txtDate);
        dialog.add(new JLabel("Пользователь:"));
        dialog.add(txtUser);

        JTextField[] extraFields = new JTextField[6];
        String[] labels;

        if (type.equals("Накладная")) {
            labels = new String[]{"Сумма:", "Валюта:", "Курс Валюты:", "Товар:", "Количество:"};
        } else if (type.equals("Платёжка")) {
            labels = new String[]{"Сумма:", "Сотрудник:"};
        } else {
            labels = new String[]{"Контрагент:", "Сумма:", "Валюта:", "Курс Валюты:", "Комиссия:"};
        }

        for (String label : labels) {
            dialog.add(new JLabel(label));
            JTextField field = new JTextField();
            dialog.add(field);
            extraFields[java.util.Arrays.asList(labels).indexOf(label)] = field;
        }

        JButton btnOk = new JButton("ОК");
        JButton btnCancel = new JButton("Отмена");
        dialog.add(btnOk);
        dialog.add(btnCancel);

        btnOk.addActionListener(e -> {
            try {
                String number = txtNumber.getText();
                LocalDate date = LocalDate.parse(txtDate.getText(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                String user = txtUser.getText();

                Document doc = null;
                if (type.equals("Накладная")) {
                    doc = new Invoice(number, date, user,
                        Double.parseDouble(extraFields[0].getText()),
                        extraFields[1].getText(),
                        Double.parseDouble(extraFields[2].getText()),
                        extraFields[3].getText(),
                        Double.parseDouble(extraFields[4].getText()));
                } else if (type.equals("Платёжка")) {
                    doc = new Payment(number, date, user,
                        Double.parseDouble(extraFields[0].getText()),
                        extraFields[1].getText());
                } else if (type.equals("Заявка на оплату")) {
                    doc = new PaymentRequest(number, date, user,
                        extraFields[0].getText(),
                        Double.parseDouble(extraFields[1].getText()),
                        extraFields[2].getText(),
                        Double.parseDouble(extraFields[3].getText()),
                        Double.parseDouble(extraFields[4].getText()));
                }

                if (doc != null) {
                    documents.add(doc);
                    manager.addDocument(doc);
                    refreshList();
                    dialog.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Ошибка ввода: " + ex.getMessage());
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void viewDocument() {
        // Найти первый выбранный чекбокс
        Document selected = null;
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                selected = documents.get(i);
                break;
            }
        }

        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Выберите документ!");
            return;
        }

        // Отдельное окно просмотра
        JFrame viewFrame = new JFrame(selected.getTypeName());
        viewFrame.setSize(400, 300);
        viewFrame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea(selected.getDetails());
        textArea.setEditable(false);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        viewFrame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOk = new JButton("Ok");
        btnOk.addActionListener(e -> viewFrame.dispose());
        bottomPanel.add(btnOk);
        viewFrame.add(bottomPanel, BorderLayout.SOUTH);

        viewFrame.setLocationRelativeTo(this);
        viewFrame.setVisible(true);
    }

    private void saveToFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                manager.saveToFile(chooser.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Сохранено успешно!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка сохранения: " + ex.getMessage());
            }
        }
    }

    private void loadFromFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                DocumentManager newManager = new DocumentManager();
                newManager.loadFromFile(chooser.getSelectedFile());
                documents.clear();
                documents.addAll(newManager.getDocuments());
                manager = newManager;
                refreshList();
                JOptionPane.showMessageDialog(this, "Загружено успешно!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка загрузки: " + ex.getMessage());
            }
        }
    }

    private void deleteSelected() {
        List<Document> toRemove = new ArrayList<>();
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                toRemove.add(documents.get(i));
            }
        }

        if (toRemove.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Выберите документы для удаления!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Удалить выбранные документы?", "Подтверждение", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            documents.removeAll(toRemove);
            manager.removeDocuments(toRemove);
            refreshList();
        }
    }

    public static void main(String[] args) {
        // Устанавливаем кодировку UTF-8
        System.setProperty("file.encoding", "UTF-8");

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            MainApp app = new MainApp();
            app.setVisible(true);
        });
    }
}