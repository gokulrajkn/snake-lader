/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import static sl.SL.view;
import static sl.home.snakeCol;

/**
 *
 * @author bayasys
 */
public class home extends javax.swing.JPanel implements KeyListener {

    public static Integer topMargin = 30, leftMargin = 30, rightMargin = 30, bottmMargin = 30;
    public static Integer colSize = 40;
    public static Integer gamemode = 0;
    public static Integer dicemode = 0;
    public static Integer lastdice = 0;
    public static Integer s100overflow = 0;
    public static Integer snakecolyn = 0;
    public static Integer ladercolyn = 0;
    public static Integer noOfPlayers = 6;
    public static Integer curentplayer = 0;
    public static String[] players = {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7", "Player 8", "Player 9", "Player 10"};
    public static int[] playerscore = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static JPanel settingsHeaderPanel, PlayerListPanel, GameStartPanel;
    public static String[] colorAry = {"#FF0000", "#008000", "#0000FF", "#00BFFF", "#FF5733", "#FF1493", "#FFFF00", "#00FFFF", "#FFFFFF", "#000000"};
    public static Color[] javacolorAry = {
        new Color(255, 0, 0),
        new Color(0, 128, 0),
        new Color(0, 0, 255),
        new Color(0, 191, 255),
        new Color(255, 81, 57),
        new Color(255, 20, 147),
        new Color(255, 255, 0),
        new Color(0, 255, 255),
        new Color(255, 255, 255),
        new Color(0, 0, 0)
    };
    public Snake snake = new Snake();
    public static Color snakeCol = new Color(0, 128, 0);
    public static Color laderCol = new Color(255, 0, 0);
    public Point2D[] snHdpoint;
    public Point2D[] snEdpoint;
    public Point2D[] ldStpoint;
    public Point2D[] ldEdpoint;
    public int[] laderscores;
    public int[] snakescores;
    public int[] laderscoresends;
    public int[] snakescoresends;
    public static Color bgcolor = Color.gray;

    public home() {
        try {
            getPropertiesFile();
            initComponents();
            view = this;
            initView();
        } catch (IOException | JSONException ex) {
            System.out.println(ex);
        }
    }

    public void initView() {
        view.setBounds(0, 0, 850, 650);
        setBackground(bgcolor);
        int[] temp;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JPanel colPanel = new JPanel();
                colPanel.add(new JLabel(getColNumberAsString(i, j), (int) CENTER_ALIGNMENT));
                view.add(colPanel);
                colPanel.setBounds((i * colSize) + leftMargin, (j * colSize) + topMargin, colSize, colSize);
                colPanel.setBorder(BorderFactory.createLineBorder(Color.black));
                colPanel.setOpaque(false);
            }
        }
        // Setting Header Panel
        JPanel setttings = new JPanel();
        setttings.setBackground(bgcolor);
        settingsHeaderPanel = setttings;
        JLabel setHeader = new JLabel("Setttings", (int) CENTER_ALIGNMENT);
        setttings.add(setHeader);
        setttings.setBounds((colSize * 10) + leftMargin, topMargin - 20, 840 - ((colSize * 10) + leftMargin), 20);
        view.add(setttings);
        //Settings toolbar panel
        JPanel setToolbar = new JPanel();
        setToolbar.setBackground(bgcolor);
        PlayerListPanel = setToolbar;
        GridLayout lo = new GridLayout(0, 2, 10, 5);
        setToolbar.setLayout(lo);
        JLabel playerCountLbl = new JLabel("                         No of Players", (int) CENTER_ALIGNMENT);
        setToolbar.add(playerCountLbl);
        SpinnerModel sm = new SpinnerNumberModel((int) noOfPlayers, 1, 10, 1);
        JSpinner playercount = new JSpinner(sm);
        playercount.setPreferredSize(new Dimension(65, 25));
        playercount.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                noOfPlayers = (Integer) playercount.getValue();
                playerNoChange();
            }
        });
        setToolbar.add(playercount);
        JTextField tempTxtFld;
        for (int i = 0; i < 10; i++) {
            setToolbar.add(new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><font size =8 color=\"" + colorAry[i] + "\">&#9724;</b></html>", (int) LEFT_ALIGNMENT));
            tempTxtFld = new JTextField(players[i]);
            tempTxtFld.setName("PlayerNameTxtField" + String.valueOf(i + 1));
            tempTxtFld.addFocusListener(new FocusListener() {
                public void focusLost(FocusEvent e) {
                    if (!e.isTemporary()) {
                        validatePlayerName();
                    }
                }

                @Override
                public void focusGained(FocusEvent e) {
                }
            });
            tempTxtFld.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    playerNameChange();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    playerNameChange();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {

                }
            });
            setToolbar.add(tempTxtFld);
            if (i >= noOfPlayers) {
                tempTxtFld.setEditable(false);
                tempTxtFld.setBackground(bgcolor);
            }
        }
        setToolbar.setBounds((colSize * 10) + leftMargin, topMargin + 10, 840 - ((colSize * 10) + leftMargin), 360);
        view.add(setToolbar);
        //Game Starting Panel panel
        JPanel startGamePanel = new JPanel();
        startGamePanel.setBackground(bgcolor);
        GameStartPanel = startGamePanel;
        JButton tossBtn = new JButton("TOSS");
        JButton startBtn = new JButton("Start Game");
        tossBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tossBtn.setVisible(false);
                String[] nameAry = new String[noOfPlayers];
                for (int i = 0; i < noOfPlayers; i++) {
                    nameAry[i] = players[i];
                }
                nameAry = randomSort(nameAry);
                for (int i = 0; i < noOfPlayers; i++) {
                    players[i] = nameAry[i];
                    ((JTextField) SL.getComponentByName("PlayerNameTxtField" + String.valueOf(i + 1), PlayerListPanel)).setText(nameAry[i]);
                }
                startBtn.setVisible(true);
            }
        });
        startGamePanel.add(tossBtn);
        startGamePanel.add(startBtn);
        startBtn.setVisible(false);
        startBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startBtn.setVisible(false);
                startGame();
            }
        });
        startGamePanel.setBounds((colSize * 10) + leftMargin, topMargin + 375, 840 - ((colSize * 10) + leftMargin), 40);
        view.add(startGamePanel);
    }

    public void getPropertiesFile() throws FileNotFoundException, JSONException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File("").getAbsolutePath() + File.separatorChar + "src" + File.separatorChar + "sl" + File.separatorChar + "ViewProperties.json"));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }
        JSONObject jsonObject = new JSONObject(sb.toString());
        JSONArray ary = new JSONArray();
        if (jsonObject != null && jsonObject.has("snakes") && jsonObject.getJSONArray("snakes") != null && jsonObject.getJSONArray("snakes").length() > 0) {
            ary = jsonObject.getJSONArray("snakes");
            JSONObject snake = new JSONObject();
            snHdpoint = new Point2D[ary.length()];
            snEdpoint = new Point2D[ary.length()];
            snakescores = new int[ary.length()];
            snakescoresends = new int[ary.length()];
            int[] hd = new int[ary.length()];
            int[] ed = new int[ary.length()];
            for (int i = 0; i < ary.length(); i++) {
                snake = (JSONObject) ary.get(i);
                if (snake != null && snake.has("from") && snake.has("to") && !snake.getString("from").equals("null") && !snake.getString("from").equals("") && !snake.getString("to").equals("null") && !snake.getString("to").equals("")
                        && snake.getInt("from") > 0 && snake.getInt("from") > 0) {
                    snakescores[i] = snake.getInt("from");
                    snakescoresends[i] = snake.getInt("to");
                    hd = getAxiesPointsOfScore(snake.getInt("from"));
                    ed = getAxiesPointsOfScore(snake.getInt("to"));
                    snHdpoint[i] = new Point2D.Double(((hd[0] * colSize) + leftMargin + colSize / 2), ((hd[1] * colSize) + topMargin + colSize / 2));
                    snEdpoint[i] = new Point2D.Double(((ed[0] * colSize) + leftMargin + colSize / 2), ((ed[1] * colSize) + topMargin + colSize / 2));
                }
            }
        }
        if (jsonObject != null && jsonObject.has("ladders") && jsonObject.getJSONArray("ladders") != null && jsonObject.getJSONArray("ladders").length() > 0) {
            ary = jsonObject.getJSONArray("ladders");
            JSONObject laders = new JSONObject();
            ldStpoint = new Point2D[ary.length()];
            ldEdpoint = new Point2D[ary.length()];
            laderscores = new int[ary.length()];
            laderscoresends = new int[ary.length()];
            int[] st = new int[ary.length()];
            int[] end = new int[ary.length()];
            for (int i = 0; i < ary.length(); i++) {
                laders = (JSONObject) ary.get(i);
                if (laders != null && laders.has("from") && laders.has("to") && !laders.getString("from").equals("null") && !laders.getString("from").equals("") && !laders.getString("to").equals("null") && !laders.getString("to").equals("")
                        && laders.getInt("from") > 0 && laders.getInt("from") > 0) {
                    laderscores[i] = laders.getInt("from");
                    laderscoresends[i] = laders.getInt("to");
                    st = getAxiesPointsOfScore(laders.getInt("from"));
                    end = getAxiesPointsOfScore(laders.getInt("to"));
                    ldStpoint[i] = new Point2D.Double(((st[0] * colSize) + leftMargin + colSize / 2), ((st[1] * colSize) + topMargin + colSize / 2));
                    ldEdpoint[i] = new Point2D.Double(((end[0] * colSize) + leftMargin + colSize / 2), ((end[1] * colSize) + topMargin + colSize / 2));
                }
            }
        }
    }

    public String getColNumberAsString(int i, int j) {
        int col = 0;
        if (j % 2 != 0) {
            col = ((9 - j) * 10) + (i + 1);
        } else {
            col = ((9 - j) * 10) + (10 - i);
        }
        return String.valueOf(col);
    }

    public int[] getAxiesPointsOfScore(int score) {
        int[] col = new int[2];
        int i, j;
        j = 9 - ((score - 1) / 10);
        if (j % 2 != 0) {
            if (score % 10 == 0) {
                i = 9;
            } else {
                i = (score % 10) - 1;
            }
        } else {
            if (score % 10 == 0) {
                i = 0;
            } else {
                i = 10 - (score % 10);
            }
        }
        col[0] = i;
        col[1] = j;
        return col;
    }

    public String[] randomSort(String[] names) {
        Collections.shuffle(Arrays.asList(names));
        return names;
    }

    public void playerNameChange() {
        String name;
        for (int i = 0; i < noOfPlayers; i++) {
            name = ((JTextField) SL.getComponentByName("PlayerNameTxtField" + String.valueOf(i + 1), PlayerListPanel)).getText();
            if (name != null && !name.equalsIgnoreCase("null") && !name.equalsIgnoreCase("")) {
                players[i] = name;
            }
        }
    }

    public int isSnake(int score) {
        int to = 0;
        for (int i = 0; i < snakescores.length; i++) {
            if (snakescores[i] == score) {
                to = snakescoresends[i];
            }
        }
        return to;
    }

    public int isLader(int score) {
        int to = 0;
        for (int i = 0; i < laderscores.length; i++) {
            if (laderscores[i] == score) {
                to = laderscoresends[i];
            }
        }
        return to;
    }

    public void playerNoChange() {
        JTextField txtfield = new JTextField();
        for (int i = 0; i < 10; i++) {
            txtfield = ((JTextField) SL.getComponentByName("PlayerNameTxtField" + String.valueOf(i + 1), PlayerListPanel));
            if (i < noOfPlayers) {
                txtfield.setEditable(true);
                txtfield.setBackground(Color.white);
            } else {
                txtfield.setEditable(false);
                txtfield.setBackground(bgcolor);
            }
        }
    }

    public void validatePlayerName() {
        String name;
        boolean err = false;
        JTextField errfield = new JTextField();
        for (int i = 0; i < noOfPlayers; i++) {
            name = ((JTextField) SL.getComponentByName("PlayerNameTxtField" + String.valueOf(i + 1), PlayerListPanel)).getText();
            if ((name == null || name.equalsIgnoreCase("null") || name.equalsIgnoreCase("")) && !err) {
                err = true;
                errfield = ((JTextField) SL.getComponentByName("PlayerNameTxtField" + String.valueOf(i + 1), PlayerListPanel));
            }
        }
        if (err) {
            JOptionPane.showMessageDialog(null, "Error: Please enter player name", "Error Massage", JOptionPane.ERROR_MESSAGE);
            errfield.setText("Enter Player Name");
            errfield.requestFocusInWindow();
            errfield.selectAll();
        }
    }

    public void startGame() {
        gamemode = 1;
        repaint();
    }

    public int rolldice() {
        Random ran = new Random();
        return 1 + ran.nextInt(6);
    }

    private static void getLadder(
            Graphics2D g, Point2D pt0, Point2D pt1) {
        double ladderWidth = colSize / 2;
        double distanceBetweenSteps = (colSize / 2) + 30;
        double strokeWidth = colSize / 8;

        double dx = pt1.getX() - pt0.getX();
        double dy = pt1.getY() - pt0.getY();
        double distance = pt1.distance(pt0);

        double dirX = dx / distance;
        double dirY = dy / distance;

        double offsetX = dirY * ladderWidth * 0.5;
        double offsetY = -dirX * ladderWidth * 0.5;

        Line2D lineR = new Line2D.Double(
                pt0.getX() + offsetX,
                pt0.getY() + offsetY,
                pt1.getX() + offsetX,
                pt1.getY() + offsetY);

        Line2D lineL = new Line2D.Double(
                pt0.getX() - offsetX,
                pt0.getY() - offsetY,
                pt1.getX() - offsetX,
                pt1.getY() - offsetY);

        drawStroke(g, lineL, strokeWidth);
        drawStroke(g, lineR, strokeWidth);

        int numSteps = (int) (distance / distanceBetweenSteps);
        for (int i = 0; i < numSteps; i++) {
            double stepOffsetX = (i + 1) * distanceBetweenSteps;
            double stepOffsetY = (i + 1) * distanceBetweenSteps;

            Line2D step = new Line2D.Double(
                    pt0.getX() + stepOffsetX * dirX - offsetX,
                    pt0.getY() + stepOffsetY * dirY - offsetY,
                    pt0.getX() + stepOffsetX * dirX + offsetX,
                    pt0.getY() + stepOffsetY * dirY + offsetY);
            drawStroke(g, step, strokeWidth);
        }
    }

    private static void drawStroke(Graphics2D g, Line2D line, double strokeWidth) {
        Stroke stroke = new BasicStroke(
                (float) strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        Shape Stroke = stroke.createStrokedShape(line);
        g.setColor(new Color(200, 100, 0));
        g.fill(Stroke);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        String displaylbl = "";
        int[] po = {-1, -1};
        if (snHdpoint != null && snHdpoint.length > 0 && snEdpoint != null && snEdpoint.length > 0) {
            for (int i = 0; i < snHdpoint.length; i++) {
                snake.setPoints(snHdpoint[i], snEdpoint[i]);
                snake.draw((Graphics2D) g);
            }
        }
        if (ldStpoint != null && ldStpoint.length > 0 && ldEdpoint != null && ldEdpoint.length > 0) {
            for (int i = 0; i < ldStpoint.length; i++) {
                g.setColor(Color.RED);
                getLadder((Graphics2D) g, ldStpoint[i], ldEdpoint[i]);
            }
        }
        if (gamemode == 1) {
            for (int i = 0; i < noOfPlayers; i++) {
                if (playerscore[i] > 0) {
                    g.setColor(javacolorAry[i]);
                    po = getAxiesPointsOfScore(playerscore[i]);
                    if (po[0] >= 0 && po[1] >= 0) {
                        g.fillRect((po[0] * colSize) + leftMargin + colSize / 4, (po[1] * colSize) + topMargin + colSize / 4, colSize / 2, colSize / 2);
                    }
                } else {
                    g.setColor(javacolorAry[i]);
                    g.fillRect(leftMargin + (i * 40) + colSize / 4, (10 * colSize) + colSize / 4 + topMargin, colSize / 2, colSize / 2);
                }
            }
            BufferedImage img = null, img1 = null;
            String filepath = new File("").getAbsolutePath() + File.separatorChar + "src" + File.separatorChar + "sl" + File.separatorChar + "dice.png";
            try {
                img = ImageIO.read(new File(filepath));
            } catch (IOException e) {
            }
            if (dicemode == 0) {
                g.setColor(javacolorAry[curentplayer]);
                g.drawChars("Press spacebar to throw dice".toCharArray(), 0, "Press spacebar to throw dice".length(), leftMargin, topMargin + (10 * colSize) + 100);
                g.drawImage(img, leftMargin + 200, topMargin + (10 * colSize) + 80, 24, 24, null);
                displaylbl = "Turn : " + players[curentplayer] + "           Score : " + playerscore[curentplayer];
                g.drawChars(displaylbl.toCharArray(), 0, displaylbl.length(), leftMargin, topMargin + (10 * colSize) + 150);
            } else {
                g.setColor(javacolorAry[curentplayer]);
                if (playerscore[curentplayer] == 100) {
                    displaylbl = players[curentplayer] + "  get  : " + lastdice + ". You won. Congrats !";
                    g.drawChars(displaylbl.toCharArray(), 0, displaylbl.length(), leftMargin, topMargin + (10 * colSize) + 100);
                } else if (playerscore[curentplayer] > 0) {
                    if (s100overflow > 0) {
                        s100overflow = 0;
                        displaylbl = players[curentplayer] + "  get  : " + lastdice + ". You need extact" + String.valueOf(100 - playerscore[curentplayer]) + "one to start ";
                        g.drawChars(displaylbl.toCharArray(), 0, displaylbl.length(), leftMargin, topMargin + (10 * colSize) + 100);
                    } else if (snakecolyn > 0) {
                        snakecolyn = 0;
                        displaylbl = players[curentplayer] + "  get  : " + lastdice + ". Ooops ! Snake swallowed you ";
                        g.drawChars(displaylbl.toCharArray(), 0, displaylbl.length(), leftMargin, topMargin + (10 * colSize) + 100);
                    } else if (ladercolyn > 0) {
                        displaylbl = players[curentplayer] + "  get  : " + lastdice + ". Congrats ! You win a lader";
                        g.drawChars(displaylbl.toCharArray(), 0, displaylbl.length(), leftMargin, topMargin + (10 * colSize) + 100);
                    } else {
                        displaylbl = players[curentplayer] + "  get  : " + lastdice;
                        g.drawChars(displaylbl.toCharArray(), 0, displaylbl.length(), leftMargin, topMargin + (10 * colSize) + 100);
                        String filepath1 = new File("").getAbsolutePath() + File.separatorChar + "src" + File.separatorChar + "sl" + File.separatorChar + "dice" + String.valueOf(lastdice) + ".png";
                        try {
                            img1 = ImageIO.read(new File(filepath1));
                        } catch (IOException e) {
                        }
                    }
                } else {
                    displaylbl = players[curentplayer] + "  get  : " + lastdice + ". But need one to start ";
                    g.drawChars(displaylbl.toCharArray(), 0, displaylbl.length(), leftMargin, topMargin + (10 * colSize) + 100);
                }
                if (playerscore[curentplayer] == 100) {
                    g.drawChars("Restart for next game".toCharArray(), 0, "Restart for next game".length(), leftMargin, topMargin + (10 * colSize) + 150);
                } else {
                    if (lastdice == 6 || ladercolyn > 0) {
                        if (ladercolyn > 0) {
                            ladercolyn = 0;
                        }
                        lastdice = 0;
                        g.setColor(javacolorAry[curentplayer]);
                        g.drawImage(img1, leftMargin + 200, topMargin + (10 * colSize) + 80, 24, 24, null);
                        g.drawChars("Congrats ! One more Chance ! Press enter key for next throw".toCharArray(), 0, "Congrats ! One more Chance ! Press enter key for next throw".length(), leftMargin, topMargin + (10 * colSize) + 150);

                    } else {
                        if (curentplayer + 1 == noOfPlayers) {
                            curentplayer = 0;
                        } else {
                            curentplayer++;
                        }
                        lastdice = 0;
                        g.setColor(javacolorAry[curentplayer]);
                        g.drawImage(img1, leftMargin + 200, topMargin + (10 * colSize) + 80, 24, 24, null);
                        g.drawChars("Press enter key for next throw".toCharArray(), 0, "Press enter key for next throw".length(), leftMargin, topMargin + (10 * colSize) + 150);
                    }
                }
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int dice;
        if (e.getKeyCode() == KeyEvent.VK_SPACE && gamemode == 1 && dicemode == 0) {
            dicemode = 1;
            dice = rolldice();
            if (playerscore[curentplayer] > 0 || dice == 1) {
                playerscore[curentplayer] += dice;
            } else {
            }
            if (playerscore[curentplayer] > 100) {
                playerscore[curentplayer] -= dice;
                s100overflow = 1;
            } else {
                if (isSnake(playerscore[curentplayer]) != 0) {
                    playerscore[curentplayer] = isSnake(playerscore[curentplayer]);
                    snakecolyn = 1;
                } else if (isLader(playerscore[curentplayer]) != 0) {
                    playerscore[curentplayer] = isLader(playerscore[curentplayer]);
                    ladercolyn = 1;
                }
            }
            lastdice = dice;
            repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER && gamemode == 1 && dicemode == 1) {
            dicemode = 0;
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

class Snake {

    public Point2D point0 = new Point2D.Double(100, 500);
    public Point2D point1 = new Point2D.Double(700, 700);

    double bodyWidth = 5;
    int waves = 4;
    double waveHeight = 0.05;
    double tailStart = 0.8;
    double headLength = 10;
    double headWidth = 8;
    double eyeRadius = 3;
    double irisRadius = 1.5;

    public Shape body;
    public Shape head;
    public Shape eyeR;
    public Shape eyeL;
    public Shape irisR;
    public Shape irisL;

    void setPoints(Point2D point0, Point2D point1) {
        this.point0.setLocation(point0);
        this.point1.setLocation(point1);

        AffineTransform at = AffineTransform.getRotateInstance(
                getAngle(), point0.getX(), point0.getY());
        at.translate(point0.getX(), point0.getY());

        createBody(at);
        createHead(at);
    }

    void draw(Graphics2D g) {
        g.setColor(snakeCol);
        g.fill(body);
        g.fill(head);
        g.setColor(Color.WHITE);
        g.fill(eyeR);
        g.fill(eyeL);
        g.setColor(Color.BLACK);
        g.fill(irisR);
        g.fill(irisL);
    }

    private void createBody(AffineTransform at) {
        double distance = point1.distance(point0);
        int steps = 100;
        Path2D body = new Path2D.Double();
        Point2D previousPoint = null;
        for (int i = 0; i < steps; i++) {
            double alpha = (double) i / (steps - 1);
            Point2D point = computeCenterPoint(alpha, distance);
            if (previousPoint != null) {
                Point2D bodyPoint
                        = computeBodyPoint(alpha, point, previousPoint);
                if (i == 1) {
                    body.moveTo(bodyPoint.getX(), bodyPoint.getY());
                } else {
                    body.lineTo(bodyPoint.getX(), bodyPoint.getY());
                }
            }
            previousPoint = point;
        }
        previousPoint = null;
        for (int i = steps - 1; i >= 0; i--) {
            double alpha = (double) i / (steps - 1);
            Point2D point = computeCenterPoint(alpha, distance);
            if (previousPoint != null) {
                Point2D bodyPoint
                        = computeBodyPoint(alpha, point, previousPoint);
                body.lineTo(bodyPoint.getX(), bodyPoint.getY());
            }
            previousPoint = point;
        }
        this.body = at.createTransformedShape(body);
    }

    private Point2D computeBodyPoint(
            double alpha, Point2D point, Point2D previousPoint) {
        double dx = point.getX() - previousPoint.getX();
        double dy = point.getY() - previousPoint.getY();
        double rdx = -dy;
        double rdy = dx;
        double d = Math.hypot(dx, dy);
        double localBodyWidth = bodyWidth;
        if (alpha > tailStart) {
            localBodyWidth *= (1 - (alpha - tailStart) / (1.0 - tailStart));
        }
        double px = point.getX() + rdx * (1.0 / d) * localBodyWidth;
        double py = point.getY() + rdy * (1.0 / d) * localBodyWidth;
        return new Point2D.Double(px, py);
    }

    private Point2D computeCenterPoint(
            double alpha, double distance) {
        double r = alpha * Math.PI * 2 * waves;
        double verticalScaling = 1 - (alpha * 2 - 1) * (alpha * 2 - 1);
        double y = Math.sin(r) * distance * waveHeight * verticalScaling;
        double x = alpha * distance;
        return new Point2D.Double(x, y);
    }

    private void createHead(AffineTransform at) {
        Shape head = new Ellipse2D.Double(
                -headLength, -headWidth,
                headLength + headLength,
                headWidth + headWidth);
        this.head = at.createTransformedShape(head);

        Shape eyeR = new Ellipse2D.Double(
                -headLength * 0.5 - eyeRadius,
                -headWidth * 0.6 - eyeRadius,
                eyeRadius + eyeRadius,
                eyeRadius + eyeRadius);
        Shape eyeL = new Ellipse2D.Double(
                -headLength * 0.5 - eyeRadius,
                headWidth * 0.6 - eyeRadius,
                eyeRadius + eyeRadius,
                eyeRadius + eyeRadius);
        this.eyeR = at.createTransformedShape(eyeR);
        this.eyeL = at.createTransformedShape(eyeL);

        Shape irisR = new Ellipse2D.Double(
                -headLength * 0.4 - eyeRadius,
                -headWidth * 0.6 - irisRadius,
                irisRadius + irisRadius,
                irisRadius + irisRadius);
        Shape irisL = new Ellipse2D.Double(
                -headLength * 0.4 - eyeRadius,
                headWidth * 0.6 - irisRadius,
                irisRadius + irisRadius,
                irisRadius + irisRadius);
        this.irisR = at.createTransformedShape(irisR);
        this.irisL = at.createTransformedShape(irisL);
    }

    private double getAngle() {
        double dx = point1.getX() - point0.getX();
        double dy = point1.getY() - point0.getY();
        double angleRad = Math.atan2(dy, dx);
        return angleRad;
    }
}
