import java.awt.Color;

public class RBT<T extends Comparable<T>> {
    public Nodo<T> root;
    public int size;
    
    public RBT() {
        this.root = null;
        this.size = 0;
    }

    private void rotateLeft(Nodo<T> x) {
        Nodo<T> y = x.right;
        x.right = y.left;
        
        if (y.left != null) {
            y.left.parent = x;
        }
        
        y.parent = x.parent;
        
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Nodo<T> y) {
        Nodo<T> x = y.left;
        y.left = x.right;
        
        if (x.right != null) {
            x.right.parent = y;
        }
        
        x.parent = y.parent;
        
        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }
        
        x.right = y;
        y.parent = x;
    }

    public void insert(T elemento) {
        Nodo<T> newNode = new Nodo<>(elemento);
        size++;
        
        if (root == null) {
            root = newNode;
            root.color = Color.BLACK;
            return;
        }
        
        Nodo<T> actual = root;
        while (true) {
            if (actual.elemento.compareTo(elemento) > 0) {
                if (actual.left == null) {
                    actual.left = newNode;
                    newNode.parent = actual;
                    break;
                }
                actual = actual.left;
            } else {
                if (actual.right == null) {
                    actual.right = newNode;
                    newNode.parent = actual;
                    break;
                }
                actual = actual.right;
            }
        }
        
        fixInsert(newNode);
    }

    private void fixInsert(Nodo<T> k) {
        while (k.parent != null && k.parent.color == Color.RED) {
            if (k.parent == k.parent.parent.left) {
                Nodo<T> tio = k.parent.parent.right;
                
                if (tio != null && tio.color == Color.RED) {
                    k.parent.color = Color.BLACK;
                    tio.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        rotateLeft(k);
                    }
                    k.parent.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    rotateRight(k.parent.parent);
                }
            } else {
                Nodo<T> tio = k.parent.parent.left;
                
                if (tio != null && tio.color == Color.RED) {
                    k.parent.color = Color.BLACK;
                    tio.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rotateRight(k);
                    }
                    k.parent.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    rotateLeft(k.parent.parent);
                }
            }
        }
        root.color = Color.BLACK;
    }

    // Buscar un nodo
    private Nodo<T> search(T elemento) {
        Nodo<T> actual = root;
        while (actual != null) {
            int cmp = actual.elemento.compareTo(elemento);
            if (cmp == 0) {
                return actual;
            } else if (cmp > 0) {
                actual = actual.left;
            } else {
                actual = actual.right;
            }
        }
        return null;
    }

    private Nodo<T> minimum(Nodo<T> nodo) {
        while (nodo.left != null) {
            nodo = nodo.left;
        }
        return nodo;
    }

    private void transplant(Nodo<T> u, Nodo<T> v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    public boolean delete(T elemento) {
        Nodo<T> z = search(elemento);
        if (z == null) {
            return false;
        }
        
        size--;
        Nodo<T> y = z;
        Nodo<T> x;
        Nodo<T> xParent;
        Color yOriginalColor = y.color;
        
        if (z.left == null) {
            x = z.right;
            xParent = z.parent;
            transplant(z, z.right);
        } else if (z.right == null) {
            x = z.left;
            xParent = z.parent;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            xParent = y;
            
            if (y.parent == z) {
                if (x != null) x.parent = y;
            } else {
                xParent = y.parent;
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        
        if (yOriginalColor == Color.BLACK) {
            fixDelete(x, xParent);
        }
        
        return true;
    }

    private void fixDelete(Nodo<T> x, Nodo<T> xParent) {
        while (x != root && (x == null || x.color == Color.BLACK)) {
            if (x == (xParent != null ? xParent.left : null)) {
                Nodo<T> w = xParent.right;
                
                if (w != null && w.color == Color.RED) {
                    w.color = Color.BLACK;
                    xParent.color = Color.RED;
                    rotateLeft(xParent);
                    w = xParent.right;
                }
                
                if (w != null) {
                    if ((w.left == null || w.left.color == Color.BLACK) &&
                        (w.right == null || w.right.color == Color.BLACK)) {
                        w.color = Color.RED;
                        x = xParent;
                        xParent = x.parent;
                    } else {
                        if (w.right == null || w.right.color == Color.BLACK) {
                            if (w.left != null) w.left.color = Color.BLACK;
                            w.color = Color.RED;
                            rotateRight(w);
                            w = xParent.right;
                        }
                        w.color = xParent.color;
                        xParent.color = Color.BLACK;
                        if (w.right != null) w.right.color = Color.BLACK;
                        rotateLeft(xParent);
                        x = root;
                        break;
                    }
                } else {
                    break;
                }
            } else {
                Nodo<T> w = xParent != null ? xParent.left : null;
                
                if (w != null && w.color == Color.RED) {
                    w.color = Color.BLACK;
                    xParent.color = Color.RED;
                    rotateRight(xParent);
                    w = xParent.left;
                }
                
                if (w != null) {
                    if ((w.right == null || w.right.color == Color.BLACK) &&
                        (w.left == null || w.left.color == Color.BLACK)) {
                        w.color = Color.RED;
                        x = xParent;
                        xParent = x.parent;
                    } else {
                        if (w.left == null || w.left.color == Color.BLACK) {
                            if (w.right != null) w.right.color = Color.BLACK;
                            w.color = Color.RED;
                            rotateLeft(w);
                            w = xParent.left;
                        }
                        w.color = xParent.color;
                        xParent.color = Color.BLACK;
                        if (w.left != null) w.left.color = Color.BLACK;
                        rotateRight(xParent);
                        x = root;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        if (x != null) x.color = Color.BLACK;
    }

    public void inOrder() {
        if (root == null) {
            System.out.println("El árbol está vacío");
            return;
        }
        inOrderRecursivo(root);
    }

    private void inOrderRecursivo(Nodo<T> nodo) {
        if (nodo.left != null) {
            inOrderRecursivo(nodo.left);
        }
        System.out.println(nodo);
        if (nodo.right != null) {
            inOrderRecursivo(nodo.right);
        }
    }
}