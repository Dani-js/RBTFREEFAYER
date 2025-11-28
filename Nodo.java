
import java.awt.Color;

public class Nodo <T extends Comparable<T>> {
    T elemento;
    Nodo<T> parent;
    Nodo<T> left;
    Nodo<T> right;
    Color color; // Nuevo atributo para el color del nodo

    public Nodo(T elemento) {
        this.elemento = elemento;
        this.parent = null;
        this.left = null;
        this.right = null;
        this.color = Color.RED; // Inicializar el color del nodo como rojo
    }

    public Nodo<T> getLeft() {
        return left;
    }
    public Nodo<T> getRight() {
        return right;
    }

    @Override
    public String toString() {
        return ""+elemento + 
        ", P: " +(parent != null ? parent.elemento : "/") +
        ", L: " +(left != null ? left.elemento : "/") + 
        ", R: " +(right != null ? right.elemento : "/") + 
        ", C: " + (color == Color.RED ? "R" : "B");
        }
    }