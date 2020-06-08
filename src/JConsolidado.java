/*
 * JConsolidado.java
 *
 * Created on 12 de enero de 2008, 12:26 PM
 */

import java.awt.ScrollPane;
import java.nio.DoubleBuffer;
import javax.swing.event.*;

import java.io.*;
import java.util.*;

import javax.swing.table.*;

import java.awt.print.*;
import java.text.*;

import java.lang.Object;
import java.awt.Component;
import java.awt.Container;
import javax.swing.text.JTextComponent;

import javax.swing.*;

import jxl.write.WritableWorkbook;
import jxl.write.WritableSheet;
import jxl.write.Label;
import jxl.Workbook;
import jxl.write.WriteException;
import jxl.write.biff.WritableWorkbookImpl;
import org.omg.SendingContext.RunTimeOperations;

import java.io.IOException;
import javax.swing.JOptionPane;

import javax.swing.table.TableModel;

import java.lang.NumberFormatException;

/**
 *
 * @author  José Francisco
 */
public class JConsolidado extends javax.swing.JFrame {
    
    Vector nuevafila;
    DefaultTableModel modelotabla;
    
    int i = 0, w = 0;
    double suma1 = 0, suma2 = 0;
    
    boolean suma = false;
    
    // Demo
    
    static int numero = 0;
    static DataInputStream leer3;
    static DataOutputStream escribir3;
    static boolean resp = true; // El programa expira cuando sea falso
    static File Fichero;
    
    NumberFormat nf1 = NumberFormat.getNumberInstance();
    NumberFormat nf2 = NumberFormat.getNumberInstance();
    
    // Ficheros
    File name, modelo;
    DataOutputStream escribir;
    DataInputStream leer;
    File f;
    RandomAccessFile output, input;
    
    // Enviar a excel
    static WritableWorkbook  workbook = null;
    static WritableSheet sheet = null;
    static Label label = null;
            
    /** Creates new form JConsolidado */
    public JConsolidado() {
        initComponents();
        inicio();
        jScrollPane3.setVisible(false);
        jTable2.setVisible(false);
        jPanel2.setVisible(false);
        
        //setVisible(true);
    }
    
    public void Abrir_Accion()
    {
        // Abrir los datos
        
        JFileChooser x = new JFileChooser();              
        
        x.setFileSelectionMode( JFileChooser.FILES_ONLY );
    
        int result = x.showOpenDialog( this );
     
        // si el usuario hace click en cancel se retorna a la ventana del programa
        if ( result == JFileChooser.CANCEL_OPTION )
           return;

        // obtener el archivo seleccionado
        File name = x.getSelectedFile();
        modelo = name;
        
        // mostrar error en caso de nombre invalido
        if ( name == null || name.getName().equals(""))
           JOptionPane.showMessageDialog( this, "Nombre de archivo invalido","Nombre del Archivo Invalido", JOptionPane.ERROR_MESSAGE );
       else 
       {
          // abrir archivo
          try 
          {
              leer = new DataInputStream(new FileInputStream(name));
              Limpiar_Todo();
              leerFichero();
          }
  
          // procesa la excepcion al abrir el archivo
           catch(Exception e ) 
           {
                JOptionPane.showMessageDialog( this,"Error al abrir el archivo","Error", JOptionPane.ERROR_MESSAGE );
           }      
      }
    }
    
    public void leerFichero()
    {
        String des[] = new String[500];
        String area[] = new String[500];
        String vm2[] = new String[500];
        String vrep[] = new String[500];
        String dep[] = new String[500];
        String act[] = new String[500];
        
        int numfila = 0;
        int i2 = 0;
        boolean sum = false;
        int n = 0;
        
        double sum1 = 0, sum2 = 0;
        
        try
        {
            numfila = leer.readInt();
            i2 = leer.readInt();
            sum = leer.readBoolean();
            sum1 = leer.readDouble();
            sum2 = leer.readDouble();
            suma1 = sum1;
            suma2 = sum2;
        }
        catch(Exception e)
        {
            
        }
        
        for(n = 0; n < numfila; n++)
        {
            try
            {
                des[n] = leer.readUTF();
                area[n] = leer.readUTF();
                vm2[n] = leer.readUTF();
                vrep[n] = leer.readUTF();
                dep[n] = leer.readUTF();
                act[n] = leer.readUTF();
                
                System.out.println(String.valueOf(des[n]));
                System.out.println(String.valueOf(area[n]));
                System.out.println(String.valueOf(vm2[n]));
                System.out.println(String.valueOf(vrep[n]));
                System.out.println(String.valueOf(dep[n]));
                System.out.println(String.valueOf(act[n]));
                
                Agregar_Fila();
                jTable1.setValueAt(des[n],n,0);
                jTable1.setValueAt(area[n],n,1);
                jTable1.setValueAt(vm2[n],n,2);
                jTable1.setValueAt(vrep[n],n,3);
                jTable1.setValueAt(dep[n],n,4);
                jTable1.setValueAt(act[n],n,5);
            }
            catch(Exception e)
            {
                
            }
        }
        
        if(sum == true)
        {
            Eliminar_Fila();
            suma = false;
            Sumar_Accion();
        }
    }
    
    public void Guardar_Accion()
    {
        // Guardar los datos
        JFileChooser x = new JFileChooser();
        
   	x.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int result = x.showSaveDialog(this);
        
        if(result==JFileChooser.CANCEL_OPTION)
            return;

        File name = x.getSelectedFile();
        modelo = name;
        
        if(name==null || name.getName().equals(""))
               JOptionPane.showMessageDialog(this,"Nombre del archivo invalido","Nombre del archivo invalido",JOptionPane.ERROR_MESSAGE);
        else
        {
            try
            {
                if(modelo.exists() == true)
                {
                    int result2 = JOptionPane.showConfirmDialog((Component)null,"El archivo ya existe, desea reemplazarlo?","JConsolidado",JOptionPane.YES_NO_OPTION);

                    if(result2 == JOptionPane.YES_OPTION)
                    {
                        modelo.delete();
                        modelo = name;
                        escribir = new DataOutputStream(new FileOutputStream(modelo));      
                        escribirFichero();
                    }
                    else
                    {
                        Guardar_Accion();
                    }
                }
                else
                {
                    escribir = new DataOutputStream(new FileOutputStream(modelo + ".jc"));      
                    escribirFichero();
                }
            }
            // mostrar mensaje de error si no se puede abrir el archivo
            catch ( IOException ioException ) 
            {
                JOptionPane.showMessageDialog( this, "Error al abrir el archivo","Error", JOptionPane.ERROR_MESSAGE );
            }    
        } // fin else
    }
    
    public void escribirFichero()
    {
        String des[] = new String[500];
        String area[] = new String[500];
        String vm2[] = new String[500];
        String vrep[] = new String[500];
        String dep[] = new String[500];
        String act[] = new String[500];
                
        int numfila = jTable1.getRowCount();
        int i2 = i;
        boolean sum = suma;
        int n = 0;
        
        double sum1 = 0, sum2 = 0;
        
        try
        {
            escribir.writeInt(numfila);
            escribir.writeInt(i);
            escribir.writeBoolean(sum);
            sum1 = suma1;
            sum2 = suma2;
            escribir.writeDouble(sum1);
            escribir.writeDouble(sum2);
        }
        catch(Exception e)
        {
            
        }
        
        for(n = 0; n < jTable1.getRowCount(); n++)
        {
            try
            {
                des[n] = String.valueOf(jTable1.getValueAt(n,0));
                area[n] = String.valueOf(jTable1.getValueAt(n,1));
                vm2[n] = String.valueOf(jTable1.getValueAt(n,2));
                vrep[n] = String.valueOf(jTable1.getValueAt(n,3));
                dep[n] = String.valueOf(jTable1.getValueAt(n,4));
                act[n] = String.valueOf(jTable1.getValueAt(n,5));
                
                System.out.println(String.valueOf(des[n]));
                System.out.println(String.valueOf(area[n]));
                System.out.println(String.valueOf(vm2[n]));
                System.out.println(String.valueOf(vrep[n]));
                System.out.println(String.valueOf(dep[n]));
                System.out.println(String.valueOf(act[n]));
                
                escribir.writeUTF(des[n]);
                escribir.writeUTF(area[n]);
                escribir.writeUTF(vm2[n]);
                escribir.writeUTF(vrep[n]);
                escribir.writeUTF(dep[n]);
                escribir.writeUTF(act[n]);
            }
            catch(Exception e)
            {
                
            }
        }
    }
    
    public void inicio()
    {
        i = 0;
        w = 0;
        suma1 = 0;
        suma2 = 0;
        suma = false;
        
        nf1.setMaximumFractionDigits(2);
        nf2.setMaximumFractionDigits(0);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jButton18 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JConsolidado");
        setResizable(false);
        getContentPane().setLayout(null);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingresar datos"));
        jPanel1.setFont(new java.awt.Font("Tahoma", 0, 3));
        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel1.setText("Descripción:");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(20, 30, 70, 14);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel2.setText("Área M2:");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(40, 140, 60, 30);

        jLabel3.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel3.setText("  V / M2 US $:");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 170, 80, 30);

        jLabel4.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel4.setText(" DEP %:");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(50, 200, 40, 30);

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });
        jPanel1.add(jTextField1);
        jTextField1.setBounds(100, 140, 50, 20);

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField2KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });
        jPanel1.add(jTextField2);
        jTextField2.setBounds(100, 170, 50, 20);

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField3KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });
        jPanel1.add(jTextField3);
        jTextField3.setBounds(100, 200, 50, 20);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextArea1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextArea1KeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTextArea1);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(100, 30, 166, 96);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(220, 70, 290, 240);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane2.setViewportBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane2.setEnabled(false);
        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseClicked(evt);
            }
        });
        jScrollPane2.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                jScrollPane2ComponentAdded(evt);
            }
        });

        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Descripción", "Área M2", "V / M2 US $", "V / REP US $", "DEP %", "V / ACTUAL US $"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(20, 330, 690, 260);

        jButton1.setText("Agregar...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton1KeyPressed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(520, 80, 90, 23);

        jButton2.setText("Sumar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jButton2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton2KeyPressed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(520, 170, 90, 23);

        jButton3.setText("Limpiar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jButton3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton3KeyPressed(evt);
            }
        });
        getContentPane().add(jButton3);
        jButton3.setBounds(520, 200, 90, 23);

        jButton4.setText("Modificar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jButton4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton4KeyPressed(evt);
            }
        });
        getContentPane().add(jButton4);
        jButton4.setBounds(520, 110, 90, 23);

        jButton5.setText("Eliminar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jButton5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton5KeyPressed(evt);
            }
        });
        getContentPane().add(jButton5);
        jButton5.setBounds(520, 140, 90, 23);

        jProgressBar1.setIndeterminate(true);
        getContentPane().add(jProgressBar1);
        jProgressBar1.setBounds(0, 610, 760, 14);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Formulario", "Tabla", "Todo" }));
        getContentPane().add(jComboBox1);
        jComboBox1.setBounds(620, 200, 90, 20);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/civil.gif"))); // NOI18N
        getContentPane().add(jLabel5);
        jLabel5.setBounds(20, 100, 200, 190);

        jButton6.setText("Exportar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jButton6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton6KeyPressed(evt);
            }
        });
        getContentPane().add(jButton6);
        jButton6.setBounds(520, 230, 90, 23);

        jButton7.setText("Salir");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jButton7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton7KeyPressed(evt);
            }
        });
        getContentPane().add(jButton7);
        jButton7.setBounds(520, 260, 90, 23);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"DESCRIPCION", "AREA M2", "V / M2 US $", "V / REP US $", "DEP %", "V / ACTUAL US $"}
            },
            new String [] {
                "Descripción", "Área M2", "V / M2 US $", "V / REP US $", "DEP %", "V / ACTUAL US $"
            }
        ));
        jScrollPane3.setViewportView(jTable2);

        jPanel2.add(jScrollPane3);

        getContentPane().add(jPanel2);
        jPanel2.setBounds(690, 190, 20, 60);

        jToolBar1.setRollover(true);

        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/New.png"))); // NOI18N
        jButton18.setToolTipText("Nuevo");
        jButton18.setFocusable(false);
        jButton18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton18.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton18);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Open.png"))); // NOI18N
        jButton9.setToolTipText("Abrir");
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton9);

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Save.png"))); // NOI18N
        jButton10.setToolTipText("Guardar");
        jButton10.setFocusable(false);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton10);

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Symbol-Add.png"))); // NOI18N
        jButton11.setToolTipText("Agregar");
        jButton11.setFocusable(false);
        jButton11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton11);

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Refresh.png"))); // NOI18N
        jButton12.setToolTipText("Modificar");
        jButton12.setFocusable(false);
        jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton12);

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Trashcan_empty.png"))); // NOI18N
        jButton13.setToolTipText("Eliminar");
        jButton13.setFocusable(false);
        jButton13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton13);

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/sum.png"))); // NOI18N
        jButton14.setToolTipText("Sumar");
        jButton14.setFocusable(false);
        jButton14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton14.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton14);

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Limpiar.png"))); // NOI18N
        jButton15.setToolTipText("Limpiar");
        jButton15.setFocusable(false);
        jButton15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton15.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton15);

        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Excel.png"))); // NOI18N
        jButton16.setToolTipText("Exportar");
        jButton16.setFocusable(false);
        jButton16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton16.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton16);

        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Close.png"))); // NOI18N
        jButton17.setToolTipText("Salir");
        jButton17.setFocusable(false);
        jButton17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton17.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton17);

        getContentPane().add(jToolBar1);
        jToolBar1.setBounds(0, 0, 740, 60);

        jMenu1.setText("Archivo");

        jMenuItem13.setText("Nuevo");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem13);

        jMenuItem14.setText("Abrir");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem14);

        jMenuItem15.setText("Guardar");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem15);

        jMenuItem9.setText("Salir");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem9);

        jMenuBar1.add(jMenu1);

        jMenu4.setText("Funciones");

        jMenuItem1.setText("Agregar...");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem1);

        jMenuItem2.setText("Sumar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jMenu3.setText("Limpiar");

        jMenuItem3.setText("Formulario");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem11.setText("Tabla");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem11);

        jMenuItem12.setText("Todo");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem12);

        jMenu4.add(jMenu3);

        jMenuItem4.setText("Modificar");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem4);

        jMenuItem5.setText("Eliminar");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem5);

        jMenuItem6.setText("Exportar");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem6);

        jMenuBar1.add(jMenu4);

        jMenu2.setText("Ayuda");

        jMenuItem7.setText("Contenido de ayuda");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuItem8.setText("Autor");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuItem10.setText("Acerca de...");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem10);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-736)/2, (screenSize.height-674)/2, 736, 674);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
// TODO: Agrege su codigo aqui:
        Acerca acerca = new Acerca();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
// TODO: Agrege su codigo aqui:
        Guardar_Accion();
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
// TODO: Agrege su codigo aqui:
        Abrir_Accion();
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
// TODO: Agrege su codigo aqui:
        Limpiar_Todo();
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
// TODO: Agrege su codigo aqui:
        Limpiar_Todo();
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
// TODO: Agrege su codigo aqui:
        Limpiar_Tabla();
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
// TODO: Agrege su codigo aqui:
        Limpiar_Formulario();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
// TODO: Agrege su codigo aqui:
        Salir_Accion();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jButton7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton7KeyPressed
// TODO: Agrege su codigo aqui:
    }//GEN-LAST:event_jButton7KeyPressed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
// TODO: Agrege su codigo aqui:
        Salir_Accion();
    }//GEN-LAST:event_jButton7ActionPerformed

    public void Exportar_Accion()
    {
        try
        {  
            CrearExcel("Libro1.xls","Hoja1",jTable1.getModel(),jTable2.getModel());
            Runtime.getRuntime().exec("AbrirExcel.bat");
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Error al exportar","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void CrearExcel(String ruta,String nombreHoja,TableModel modeloTabla,TableModel modeloTabla2){
        
    try
    {
        workbook = Workbook.createWorkbook(new File(ruta));
        sheet = workbook.createSheet(nombreHoja,0);
        
        for(int i=0;i<modeloTabla2.getRowCount();i++)
        {
            for(int j=0;j<modeloTabla2.getColumnCount();j++)
            {
                Object x = modeloTabla2.getValueAt(i,j);
                    
                if(x!=null)
                    label = new Label(j,i,x.toString());
                else
                    label = new Label(j,i,"");

                sheet.addCell(label);
            }
        }
        
        for(int i=0;i<modeloTabla.getRowCount();i++)
        {
            for(int j=0;j<modeloTabla.getColumnCount();j++)
            {
                Object x = modeloTabla.getValueAt(i,j);
                    
                if(x!=null)
                    label = new Label(j,i + 1,x.toString());
                else
                    label = new Label(j,i + 1,"");

                sheet.addCell(label);
            }
        }
        
        workbook.write(); 
        workbook.close();
    }
    catch(IOException e){
        JOptionPane.showMessageDialog(null,e.toString(),"Error",JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    catch(WriteException e){
        JOptionPane.showMessageDialog(null,e.toString(),"Error",JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    }
    
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
// TODO: Agrege su codigo aqui:
        try
        {
            int f = 0;
            
            f = jTable1.getSelectedRow();

            jTextArea1.setText(String.valueOf(jTable1.getValueAt(f,0)));
            jTextField1.setText(String.valueOf(jTable1.getValueAt(f,1)));
            jTextField2.setText(String.valueOf(jTable1.getValueAt(f,2)));
            jTextField3.setText(String.valueOf(jTable1.getValueAt(f,4)));
            
            if(suma == true)
            {
                if(f == jTable1.getRowCount() - 1)
                {
                    this.Limpiar_Formulario();
                }
            }
            
            jTextArea1.requestFocus();
        }
        catch(Exception e)
        {
            
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
// TODO: Agrege su codigo aqui:
        Exportar_Accion();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jButton6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton6KeyPressed
// TODO: Agrege su codigo aqui:
    }//GEN-LAST:event_jButton6KeyPressed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
// TODO: Agrege su codigo aqui:
        Exportar_Accion();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked
// TODO: Agrege su codigo aqui:
    }//GEN-LAST:event_jScrollPane2MouseClicked

    private void jScrollPane2ComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jScrollPane2ComponentAdded
// TODO: Agrege su codigo aqui:
    }//GEN-LAST:event_jScrollPane2ComponentAdded

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
// TODO: Agrege su codigo aqui:
                
        char c= evt.getKeyChar();
        
        // Validación para que solo entren número decimales.
        
        if(!((Character.isDigit(c)) || (c==evt.VK_BACK_SPACE) || (c == evt.VK_DELETE) || (c==evt.VK_PERIOD)))
            evt.consume();
    }//GEN-LAST:event_jTextField3KeyTyped

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
// TODO: Agrege su codigo aqui:
                
        char c= evt.getKeyChar();
        
        // Validación para que solo entren número decimales.
        
        if(!((Character.isDigit(c)) || (c==evt.VK_BACK_SPACE) || (c == evt.VK_DELETE) || (c==evt.VK_PERIOD)))
            evt.consume();
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
// TODO: Agrege su codigo aqui:
                
        char c= evt.getKeyChar();
        
        // Validación para que solo entren número decimales.
        
        if(!((Character.isDigit(c)) || (c==evt.VK_BACK_SPACE) || (c == evt.VK_DELETE) || (c==evt.VK_PERIOD)))
            evt.consume();
    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTextArea1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyTyped
// TODO: Agrege su codigo aqui:
    }//GEN-LAST:event_jTextArea1KeyTyped

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
// TODO: Agrege su codigo aqui:
        Autor autor = new Autor();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
// TODO: Agrege su codigo aqui:
        try
        {
            Runtime.getRuntime().exec("Ayuda.bat");
        }
        catch(Exception e)
        {
            
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jButton5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton5KeyPressed
// TODO: Agrege su codigo aqui:
        Salir_Accion();
    }//GEN-LAST:event_jButton5KeyPressed

    private void jButton4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton4KeyPressed
// TODO: Agrege su codigo aqui:
        int KeyCode = evt.getKeyCode();
        
        if(KeyCode == 10)
        {
            Modificar_Accion();
        }
    }//GEN-LAST:event_jButton4KeyPressed

    private void jButton3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton3KeyPressed
// TODO: Agrege su codigo aqui:
        int KeyCode = evt.getKeyCode();
        
        if(KeyCode == 10)
        {
            Limpiar_Accion();
        }
    }//GEN-LAST:event_jButton3KeyPressed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
// TODO: Agrege su codigo aqui:
        Eliminar_Accion();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
// TODO: Agrege su codigo aqui:
        if(jTable1.getRowCount() == 0)
        {
            
        }
        else
        {
            Modificar_Accion();
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
// TODO: Agrege su codigo aqui:
        Sumar_Accion();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
// TODO: Agrege su codigo aqui:
        Agregar_Accion();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton2KeyPressed
// TODO: Agrege su codigo aqui:
        int KeyCode = evt.getKeyCode();
        
        if(KeyCode == 10)
        {
            Sumar_Accion();
        }
    }//GEN-LAST:event_jButton2KeyPressed

    public void Salir_Accion()
    {
        int result = JOptionPane.showConfirmDialog((Component)null,"Esta seguro que desea salir?","JConsolidado",JOptionPane.YES_NO_OPTION);
                
        if(result == JOptionPane.YES_OPTION)
        {
            JOptionPane.showMessageDialog(null,"Gracias por usar JConsolidado","JConsolidado",JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
// TODO: Agrege su codigo aqui:
        Eliminar_Accion();
    }//GEN-LAST:event_jButton5ActionPerformed

    public void Eliminar_Accion()
    {
        try
        {
            int f = 0;
            
            if(suma == true)
            {
                Eliminar_Fila();
                suma = false;
                f = -1;
            }
            else
            {
                f = jTable1.getSelectedRow();
            }
            
            if(f == -1)
            {
            
            }
            else
            {
                int result = JOptionPane.showConfirmDialog((Component)null,"Esta seguro que desea eliminar la fila seleccionada?","JConsolidado",JOptionPane.YES_NO_OPTION);
                
                if(result == JOptionPane.YES_OPTION)
                {
                    modelotabla.removeRow(f);
                }  
            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Debe seleccionar una fila","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
// TODO: Agrege su codigo aqui:
        if(jTable1.getRowCount() == 0)
        {
            
        }
        else
        {
            Modificar_Accion();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    public void Modificar_Accion()
    {
        if(jTextArea1.getText().equals("") || jTextField1.getText().equals("") || jTextField2.getText().equals("") || jTextField3.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"Por favor complete el formulario","JConsolidado",JOptionPane.ERROR_MESSAGE);
            jTextArea1.requestFocus();
        }
        else
        {
            Modificar();
        }
    }
    
    public void Modificar()
    {
        try
        {
            int f = 0;
            
            if(suma == true)
            {
                Eliminar_Fila();
                suma = false;
                f = -1;
            }
            else
            {
                f = jTable1.getSelectedRow();
            }
            
            if(f == -1)
            {
            
            }
            else
            {
                int result = JOptionPane.showConfirmDialog((Component)null,"Esta seguro que desea modificar la fila seleccionada?","JConsolidado",JOptionPane.YES_NO_OPTION);
                
                if(result == JOptionPane.YES_OPTION)
                {
                    jTable1.setValueAt(jTextArea1.getText(),f,0);
                    jTable1.setValueAt(nf1.format(Double.parseDouble(jTextField1.getText())),f,1);
                    jTable1.setValueAt(nf1.format(Double.parseDouble(jTextField2.getText())),f,2);
                    jTable1.setValueAt(nf1.format(Calcular_Rep()),f,3);
                    jTable1.setValueAt(nf2.format(Double.parseDouble(jTextField3.getText())),f,4);
                    jTable1.setValueAt(nf1.format(Calcular_Act()),f,5);
                }  
            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Debe seleccionar una fila","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public double Calcular_Rep()
    {
        return Double.parseDouble(jTextField1.getText()) * Double.parseDouble(jTextField2.getText());
    }
    
    public double Calcular_Act()
    {
        return (Double.parseDouble(jTextField1.getText()) * Double.parseDouble(jTextField2.getText())) - ((Double.parseDouble(jTextField1.getText()) * Double.parseDouble(jTextField2.getText())) * (Double.parseDouble(jTextField3.getText()) / 100));
    }
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
// TODO: Agrege su codigo aqui:
        Limpiar_Accion();
    }//GEN-LAST:event_jButton3ActionPerformed
    
    public void Limpiar_Accion()
    {
        if(jComboBox1.getSelectedIndex() == 0)
        {
            Limpiar_Formulario();
        }
            
        if(jComboBox1.getSelectedIndex() == 1)
        {
            Limpiar_Tabla();
        }
        
        if(jComboBox1.getSelectedIndex() == 2)
        {
            Limpiar_Todo();
        }
    }
    
    public void Limpiar_Todo()
    {
        Limpiar_Tabla();
        inicio();
        Limpiar_Formulario();
    }
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
// TODO: Agrege su codigo aqui:
        Sumar_Accion();
    }//GEN-LAST:event_jButton2ActionPerformed

    public void Sumar_Accion()
    {
        if(jTable1.getRowCount() == 0)
        {
            
        }
        else
        {
            if(suma == false)
            {
                Agregar_Fila();

                w = 0;
                suma1 = 0;
                suma2 = 0;
                
                for(w = 0; w < jTable1.getRowCount() - 1; w++)
                {
                    suma1 = suma1 + Double.parseDouble(JModulo.Quitar_Caracter(String.valueOf(jTable1.getValueAt(w,3)),","));
                    suma2 = suma2 + Double.parseDouble(JModulo.Quitar_Caracter(String.valueOf(jTable1.getValueAt(w,5)),","));
                }

                jTable1.setValueAt("TOTAL US $",jTable1.getRowCount() - 1,0);
                jTable1.setValueAt(nf1.format(suma1),jTable1.getRowCount() - 1,3);
                jTable1.setValueAt(nf1.format(suma2),jTable1.getRowCount() - 1,5);
                suma = true;
            }
        }
    }
    
    public void Deshabilitar()
    {
        jTextArea1.setEnabled(false);
        jTextField1.setEnabled(false);
        jTextField2.setEnabled(false);
        jTextField3.setEnabled(false);
        
        jButton1.setEnabled(false);
        jButton2.setEnabled(false);
        
        jButton4.setEnabled(false);
        jButton5.setEnabled(false);
        
        jComboBox1.setSelectedIndex(1);
        jComboBox1.setEnabled(false);
        
        jMenu1.setEnabled(false);
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO: Agrege su codigo aqui:
        Agregar_Accion();
    }//GEN-LAST:event_jButton1ActionPerformed

    public void Agregar_Accion()
    {
        if(suma == true)
        {
            Eliminar_Fila();
            suma = false;
        }
        if(jTextArea1.getText().equals("") || jTextField1.getText().equals("") || jTextField2.getText().equals("") || jTextField3.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"Por favor complete el formulario","JConsolidado",JOptionPane.ERROR_MESSAGE);
            jTextArea1.requestFocus();
        }
        else
        {
            Agregar();
        }
    }
    
    public void Agregar()
    {      
        Agregar_Fila();
        i = jTable1.getRowCount() - 1;
        jTable1.setValueAt(jTextArea1.getText(),i,0);
        jTable1.setValueAt(nf1.format(Double.parseDouble(jTextField1.getText())),i,1);
        jTable1.setValueAt(nf1.format(Double.parseDouble(jTextField2.getText())),i,2);
        jTable1.setValueAt(nf1.format(Calcular_Rep()),i,3);
        jTable1.setValueAt(nf2.format(Double.parseDouble(jTextField3.getText())),i,4);
        jTable1.setValueAt(nf1.format(Calcular_Act()),i,5);
        
        Limpiar_Formulario();
    }
    
    public void Limpiar_Formulario()
    {
        jTextArea1.setText("");
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextArea1.requestFocus();
    }
    
    public void Limpiar_Tabla()
    {
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Descripción","Área M2","V / M2 US $","V / REP US $","DEP %","V / ACTUAL US $"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        inicio();
        jTextArea1.requestFocus();
    }
    
    public void Agregar_Fila()
    {
        modelotabla = (DefaultTableModel) jTable1.getModel();
        nuevafila = new Vector();
        jTable1.setModel(modelotabla);
        nuevafila.add(null);
        modelotabla.addRow(nuevafila);
    }
    
    public void Eliminar_Fila()
    {
        int fila = jTable1.getRowCount() - 1;
        
        System.out.println(String.valueOf(fila));
        
        modelotabla.removeRow(fila);
    }
    
    private void jButton1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton1KeyPressed
// TODO: Agrege su codigo aqui:
        int KeyCode = evt.getKeyCode();
        
        if(KeyCode == 10)
        {
            Agregar_Accion();
        }
    }//GEN-LAST:event_jButton1KeyPressed

    private void jTextField3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyPressed
// TODO: Agrege su codigo aqui:
        int KeyCode = evt.getKeyCode();
        
        if(KeyCode == 10)
        {
            jButton1.requestFocus();
        }
    }//GEN-LAST:event_jTextField3KeyPressed

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyPressed
// TODO: Agrege su codigo aqui:
        int KeyCode = evt.getKeyCode();
        
        if(KeyCode == 10)
        {
            jTextField3.requestFocus();
        }
    }//GEN-LAST:event_jTextField2KeyPressed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
// TODO: Agrege su codigo aqui:
        int KeyCode = evt.getKeyCode();
        
        if(KeyCode == 10)
        {
            jTextField2.requestFocus();
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jTextArea1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyPressed
// TODO: Agrege su codigo aqui:
        int KeyCode = evt.getKeyCode();
        
        if(KeyCode == 10)
        {
            jTextField1.requestFocus();
        }
    }//GEN-LAST:event_jTextArea1KeyPressed

private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
// TODO add your handling code here:
    this.Abrir_Accion();
}//GEN-LAST:event_jButton9ActionPerformed

private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
// TODO add your handling code here:
    this.Guardar_Accion();
}//GEN-LAST:event_jButton10ActionPerformed

private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
// TODO add your handling code here:
    this.Agregar_Accion();
}//GEN-LAST:event_jButton11ActionPerformed

private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
// TODO add your handling code here:
    this.Modificar_Accion();
}//GEN-LAST:event_jButton12ActionPerformed

private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
// TODO add your handling code here:
    this.Eliminar_Accion();
}//GEN-LAST:event_jButton13ActionPerformed

private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
// TODO add your handling code here:
    this.Sumar_Accion();
}//GEN-LAST:event_jButton14ActionPerformed

private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
// TODO add your handling code here:
    this.Limpiar_Accion();
}//GEN-LAST:event_jButton15ActionPerformed

private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
// TODO add your handling code here:
    this.Exportar_Accion();
}//GEN-LAST:event_jButton16ActionPerformed

private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
// TODO add your handling code here:
    this.Salir_Accion();
}//GEN-LAST:event_jButton17ActionPerformed

private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
// TODO add your handling code here:
    this.Limpiar_Todo();
}//GEN-LAST:event_jButton18ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) 
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                // Aplicar HiFiLookAndFeel a nuestra aplicación
                try
                {
                    UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
                }
                catch(Exception e)
                {
                        try
                        {
                            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                        }
                        catch(Exception err)
                        {
                            System.out.println("Error loading myXPStyleTheme: " + err.toString());
                        }
                }
                
                /*if(Fecha_Expiracion() == true)
                {
                    Final();
                }
                else
                {
                    Expiracion();
                }*/
                new JConsolidado().setVisible(true);
            }
        });
        //JConsolidado consolidado = new JConsolidado();
        //consolidado.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public static boolean Fecha_Expiracion()
    {
        Date hoy = new Date();
        Date fecha = new Date();
        
        fecha.setYear(2009); // Esta es la fecha de expiración.
        
        //System.out.println(hoy.getYear() + 1900);
        //System.out.println(fecha.getYear());
        
        if((hoy.getYear() + 1900) >= fecha.getYear())        
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static void Final()
    {
        JOptionPane.showMessageDialog(null,"El programa ha expirado, si desea comprarlo envíe un mensaje a: ingenierojosefrancisco@gmail.com","JConsolidado",JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
    
    public static void Expiracion()
    {
        Fichero = new File("Config.exe");
                
        if(!Fichero.exists())
        {
             JOptionPane.showMessageDialog(null,"Error, el programa necesita el archivo Config.exe","Error",JOptionPane.ERROR_MESSAGE);
             System.exit(0);
        }
        else
        {
             if(Demostrar() == false)
             {
                  Final();
             }
             else
             {
                  new JConsolidado().setVisible(true);
             }
        }
    }
    
    public static boolean Demostrar()
    {
        Fichero = new File("Config.exe");
        
	leerFichero3();
	escribirFichero3();
		
	return condicionar();
    }
    
    public static void leerFichero3()
    {
	// abrir archivo
	try
	{
		leer3 = new DataInputStream(new FileInputStream(Fichero));
		int num;
                
		num = leer3.readInt();
		numero = num;
                
                System.out.println(String.valueOf(numero));
                
		leer3.close();
	}
			
	// procesa la excepcion al abrir el archivo
	catch(Exception e)
	{
		//JOptionPane.showMessageDialog(this,"Error al abrir el archivo","Error",JOptionPane.ERROR_MESSAGE);
	}
    }
		
    public static void escribirFichero3()
    {
	try
	{
		escribir3 = new DataOutputStream(new FileOutputStream(Fichero));
		int num = numero;
                
		num = num + 1;
		
		escribir3.writeInt(num);
		escribir3.close();
	}
	// mostrar mensaje de error si no se puede abrir el archivo
	catch(IOException ioException)
	{
		//JOptionPane.showMessageDialog(this,"Error al abrir el archivo","Error",JOptionPane.ERROR_MESSAGE);
	}
    }
	
    public static boolean condicionar()
    {
        System.out.println("Limite: 3");
        
	if(numero >= 3)
		return false;
	else
		return true;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
    
}
