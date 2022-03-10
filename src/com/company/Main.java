package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.regex.Pattern;

public class Main extends JFrame {

    public static String[] naglowki = null;
    public static Object[][] dane = null;

    public Main() {
        setSize(1500, 700);
        setTitle("Integracja systemów Lab2 - Piotr Błażewicz");
    }

    public static void importuj() {
        FileReader fr = null;
        String linia = "";

        try {
            final String fileName = "src/com/company/katalog.txt";
            fr = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Blad przy otwieraniu pliku!");
        }

        BufferedReader br = new BufferedReader(fr);
        try {
            int j = 0;
            while (null != (linia = br.readLine())) {
                String[] words = linia.split(";", -1);

                int i = 0;
                for (String word : words) {
                    if (word.isEmpty() && i < words.length - 1)
                        dane[j][i] = "Brak informacji";
                    else if (!word.isEmpty())
                        dane[j][i] = word;

                    i++;
                }
                j++;
            }
        } catch (IOException e) {
            System.out.println("Blad odczytu pliku!");
        }

        try {
            fr.close();
        } catch (IOException e) {
            System.out.println("Blad przy zamykaniu pliku!");
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        naglowki = new String[]{"Producent",
                "Przekątna",
                "Rozdzielczość",
                "Rodzaj powierzchni ekranu",
                "Ekran dotykowy",
                "Procesor",
                "Liczba rdzeni",
                "prędkość taktowania MHz",
                "RAM",
                "pojemność dysku",
                "rodzaj dysku",
                "Układ graficzny",
                "Pamięć układu graficznego",
                "System operacyjny",
                "Napęd fizyczny"};
        dane = new Object[24][15];

        Main okienko = new Main();  //okienko
        okienko.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton button_eksport = new JButton("Eksportuj dane");   //przycisk eksport
        button_eksport.setBounds(10, 450, 150, 50);
        button_eksport.setBackground(Color.ORANGE);
        button_eksport.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JButton button_import = new JButton("Importuj dane");   //przycisk importu
        button_import.setBounds(170, 450, 150, 50);
        button_import.setBackground(Color.YELLOW);
        button_import.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JTable table = new JTable(dane, naglowki) {
            @Override
            public void setValueAt(Object aValue, int row, int column) {    //edycja danych
                if (aValue.toString().trim().isEmpty()) {   //trim usuniecie bialych znakow - zeby spacje uznawalo jako puste
                    JOptionPane.showMessageDialog(okienko, "Pole nie może być puste!");
                    //System.out.println("Puste pole!");
                } else if ((column == 4 || column == 10) && aValue.toString().trim().length() != 3) {
                    JOptionPane.showMessageDialog(okienko, "Tekst musi miec 3 znaki!");
                } else if (column == 1 && !aValue.toString().endsWith("\"")) {
                    JOptionPane.showMessageDialog(okienko, "Pole musi się kończyć na \"");
                } else if (column == 2 & !aValue.toString().matches("[0-9]+x[0-9]+")) {
                    JOptionPane.showMessageDialog(okienko, "Wprowadź wartość według wzoru, np. 1920x1080");
                } else {
                    super.setValueAt(aValue, row, column);
                    dane[row][column] = aValue;
                }
            }
        };

        button_eksport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrintWriter zapis = null;
                try {
                    zapis = new PrintWriter("wynik.txt");
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

                for (int i = 0; i < 24; i++) {
                    for (int j = 0; j < 15; j++) {
                        zapis.print(dane[i][j] + ";");
                    }
                    if (i < 23)
                        zapis.print("\n");
                }
                zapis.close();
            }
        });

        button_import.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importuj();
                okienko.repaint();
            }
        });

        table.setBounds(30, 40, 200, 300);
        JScrollPane sp = new JScrollPane(table);
        okienko.add(button_eksport);
        okienko.add(button_import);
        okienko.add(sp);
        okienko.setVisible(true);
    }
}