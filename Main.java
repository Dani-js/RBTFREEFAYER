import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Main extends JFrame {
    private Font font;
    private JPanel panelArbol;
    private JTextField texto;
    private RBT<Integer> rbt = new RBT<>();
    private Map<Nodo<Integer>, Point> posiciones = new HashMap<>();
    private int nodeCount = 0;
    
    public Main() {
        super("Árbol Rojo-Negro (RBT)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(Color.BLUE);
        setLocationRelativeTo(null);
        font = new Font("Arial", Font.BOLD, 40);
        setFont(font);

        panelArbol = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarArbol((Graphics2D) g);
            }
            
            @Override
            public Dimension getPreferredSize() {
                int altura = calcularAltura(rbt.root);
                int ancho = (int) (Math.pow(2, altura) * 120);
                int alto = altura * 150 + 100;
                return new Dimension(Math.max(ancho, 1200), Math.max(alto, 700));
            }
        };
        panelArbol.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(panelArbol);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        this.add(panel, BorderLayout.NORTH);
        
        texto = new JTextField(30);
        texto.setFont(font);
        texto.setHorizontalAlignment(JTextField.CENTER);
        panel.add(texto);
        
        JButton botonInsertar = new JButton("Insertar");
        botonInsertar.setFont(font);
        botonInsertar.setBackground(new Color(46, 204, 113));
        botonInsertar.setForeground(Color.WHITE);
        botonInsertar.addActionListener(e -> this.insertar());
        panel.add(botonInsertar);
        
        JButton botonEliminar = new JButton("Eliminar");
        botonEliminar.setFont(font);
        botonEliminar.setBackground(new Color(231, 76, 60));
        botonEliminar.setForeground(Color.WHITE);
        botonEliminar.addActionListener(e -> this.eliminar());
        panel.add(botonEliminar);
        
        setVisible(true);
    }

    private int calcularAltura(Nodo<Integer> nodo) {
        if (nodo == null) return 0;
        return 1 + Math.max(calcularAltura(nodo.left), calcularAltura(nodo.right));
    }

    public void calcularPosiciones(Nodo<Integer> nodo, int nivel, Map<Nodo<Integer>, Point> posiciones, 
                                    int x, int espacioHorizontal, int espacioVertical) {
        if (nodo == null) return;
        
        int y = nivel * espacioVertical + 80;
        posiciones.put(nodo, new Point(x, y));
        
        int nuevoEspacio = espacioHorizontal / 2;
        
        if (nodo.left != null) {
            calcularPosiciones(nodo.left, nivel + 1, posiciones, x - nuevoEspacio, nuevoEspacio, espacioVertical);
        }
        if (nodo.right != null) {
            calcularPosiciones(nodo.right, nivel + 1, posiciones, x + nuevoEspacio, nuevoEspacio, espacioVertical);
        }
    }

    public void insertar() {
        try {
            int value = Integer.parseInt(texto.getText().trim());
            rbt.insert(value);
            texto.setText("");
            nodeCount++;
            redibujar();
            JOptionPane.showMessageDialog(this, "Elemento " + value + " insertado correctamente", 
                                        "Inserción exitosa", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un número válido", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminar() {
        try {
            int value = Integer.parseInt(texto.getText().trim());
            boolean eliminado = rbt.delete(value);
            texto.setText("");
            
            if (eliminado) {
                nodeCount--;
                redibujar();
                JOptionPane.showMessageDialog(this, "Elemento " + value + " eliminado correctamente", 
                                            "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "El elemento " + value + " no existe en el árbol", 
                                            "Elemento no encontrado", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un número válido", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void redibujar() {
        if (rbt.root == null) {
            posiciones.clear();
            panelArbol.revalidate();
            panelArbol.repaint();
            return;
        }
        
        int altura = calcularAltura(rbt.root);
        
        int espacioHorizontalInicial = (int) (Math.pow(2, altura - 1) * 60);
        int espacioVertical = 120;
        
        int anchoTotal = espacioHorizontalInicial * 2 + 200;
        int xCentro = anchoTotal / 2;
        
        posiciones.clear();
        calcularPosiciones(rbt.root, 0, posiciones, xCentro, espacioHorizontalInicial, espacioVertical);

        panelArbol.revalidate();
        panelArbol.repaint();
    }
    
    private void dibujarArbol(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int diameter = 80;

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        for (Map.Entry<Nodo<Integer>, Point> entry : posiciones.entrySet()) {
            Nodo<Integer> nodo = entry.getKey();
            Point p = entry.getValue();

            if (nodo.left != null && posiciones.containsKey(nodo.left)) {
                Point pLeft = posiciones.get(nodo.left);
                g2.drawLine(p.x, p.y, pLeft.x, pLeft.y);
            }
            if (nodo.right != null && posiciones.containsKey(nodo.right)) {
                Point pRight = posiciones.get(nodo.right);
                g2.drawLine(p.x, p.y, pRight.x, pRight.y);
            }
        }

        // Dibujar nodos
        Font nodeFont = new Font("Arial", Font.BOLD, 35);
        for (Map.Entry<Nodo<Integer>, Point> entry : posiciones.entrySet()) {
            Nodo<Integer> nodo = entry.getKey();
            Point p = entry.getValue();

            int x = p.x - diameter / 2;
            int y = p.y - diameter / 2;

            g2.setColor(nodo.color);
            g2.fillOval(x, y, diameter, diameter);

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(x, y, diameter, diameter);

            g2.setFont(nodeFont);
            g2.setColor(Color.WHITE);
            String text = "" + nodo.elemento;
            FontMetrics fm = g2.getFontMetrics();
            int textX = x + (diameter - fm.stringWidth(text)) / 2;
            int textY = y + ((diameter - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(text, textX, textY);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main();
        });
    }
}