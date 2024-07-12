
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class quiz extends JPanel {
    public static class JFRAME extends JFrame {
        public JFRAME() {
            super("Jeu pour enfant!");
            setSize(800, 600);
            setLocationRelativeTo(null);
            setVisible(true);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setResizable(false);
        }
    }

    private JLabel questionLabel, resultatLabel, scoreLabel, tempsRestantLabel;
    JFRAME q = new JFRAME();
    private Button[] choixButtons, fakebuttons;
    private int niveau;

    private int score;
    private int nombre1, nombre2, reponseCorrecte;
    private char operateur;
    private int numQuestion;
    public Timer chrono; // Timer pour le chronomètre7
    private int tempsRestant; // Temps restant en secondes
    private JPanel topPanel;
    public Clip timer, error, correct;
    private static CardLayout CardLayout = new CardLayout();
    private static JPanel container = new JPanel(CardLayout);
    boolean isButtonOneClicked = false;
    String[] niveaux = { "Facile", "Difficile" };
    boolean ingame;

    public quiz(String titre) {

        try {

            File fontFile = new File("Fonts/Playtime.otf");
            Font playf = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(50f);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(playf);

            // Initialisation de l'interface
            JPanel panel = new JPanel();
            ImageIcon imageIcon = new ImageIcon(quiz.this.getClass().getResource("images/Happy Morning Animation.gif"));

            JLabel background = new JLabel(imageIcon);

            imageIcon.setImageObserver(background);

            q.setContentPane(background);
            q.setLayout(new BorderLayout());
            q.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            q.setResizable(false);

            // Demander le niveau à l'utilisateur

            JPanel startpanel = Creatstart();

            panel.setOpaque(false);
            panel.setLayout(null);

            Button startButton = new Button();
            Button startButton2 = new Button();

            Font playff = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(21f);
            Font playfff = playff.deriveFont(Font.BOLD);
            JLabel labil = new JLabel("!!  CHOISI  LE  NIVEAU  !!");
            labil.setFont(playfff);
            labil.setBounds(280, 123, 250, 60);
            startButton.setText("Facile");
            startButton.setBounds(320, 200, 160, 60);
            startButton.setFont(playfff);
            startButton2.setText("Difficile");
            startButton2.setBounds(320, 260, 160, 60);
            startButton2.setFont(playfff);

            // button facile
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    numQuestion = 0;
                    niveau = 1;
                    initialiserJeu();
                    CardLayout.show(container, "panelall");

                }
            });
            // button difficile
            startButton2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    numQuestion = 0;
                    niveau = 2;
                    ingame = true;
                    if (!isButtonOneClicked) {
                        tempsRestantLabel.setText("Temps restant: ");
                        chrono = new Timer(1000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                tempsRestant--;
                                tempsRestantLabel.setText("Temps restant: " + tempsRestant + "s");
                                if (numQuestion <= 10 && ingame) {

                                    if (tempsRestant == 4) {
                                        timer = playSound(
                                                "Sounds/mixkit-tick-tock-clock-timer-1045.wav",
                                                timer);
                                    }
                                    if (tempsRestant <= 0) {
                                        resultatLabel
                                                .setText("Temps écoulé. La réponse correcte est : " + reponseCorrecte);
                                        stopChrono();
                                        stopSound(timer);
                                        initialiserJeu();
                                    }
                                }
                            }
                        });
                    }
                    initialiserJeu();

                    CardLayout.show(container, "panelall");
                    startButton2.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            isButtonOneClicked = true;
                        }
                    });
                }

            });

            // Initialisation des composants

            questionLabel = new JLabel();
            questionLabel.setFont(playf);
            resultatLabel = new JLabel();
            resultatLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));

            scoreLabel = new JLabel(" Score: 0 ");
            scoreLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));

            tempsRestantLabel = new JLabel();
            tempsRestantLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
            choixButtons = new Button[4];
            fakebuttons = new Button[6];
            for (int i = 0; i < 6; i++) {
                fakebuttons[i] = new Button();
                fakebuttons[i].setVisible(false);

            }
            for (int i = 0; i < 4; i++) {

                choixButtons[i] = new Button();

                choixButtons[i].setFont(playf);
                choixButtons[i].setPreferredSize(new Dimension(0, 0));

            }

            // Configuration de l'interface

            // Panel for the top section (Question number, Timer, Score)

            topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel quest = new JLabel("Question: " + (numQuestion - 1));
            quest.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
            topPanel.add(quest);

            topPanel.add(scoreLabel);

            // Panel for the center section (Question, Result, and Choices)
            JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

            // Panel for the question
            JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            questionPanel.add(questionLabel);
            questionPanel.setSize(20, 20);
            centerPanel.add(questionPanel);
            questionPanel.setOpaque(false);

            // Panel for the choices
            JPanel choicesPanel = new JPanel(new GridLayout(2, 4));

            choicesPanel.add(fakebuttons[0]);
            choicesPanel.add(choixButtons[0]);
            choicesPanel.add(fakebuttons[1]);
            choicesPanel.add(choixButtons[1]);
            choicesPanel.add(fakebuttons[2]);
            choicesPanel.add(fakebuttons[3]);
            choicesPanel.add(choixButtons[2]);
            choicesPanel.add(fakebuttons[4]);
            choicesPanel.add(choixButtons[3]);
            choicesPanel.add(fakebuttons[5]);

            centerPanel.add(choicesPanel);
            // imodaskdoisandas

            // Panel for the result
            JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JPanel panelall = new JPanel();
            panelall.setLayout(new BorderLayout());
            panelall.add(centerPanel, BorderLayout.CENTER);
            panelall.add(topPanel, BorderLayout.NORTH);
            panelall.add(bottomPanel, BorderLayout.SOUTH);
            resultPanel.add(resultatLabel);
            centerPanel.add(resultPanel);
            resultPanel.setOpaque(false);

            topPanel.setOpaque(false);
            bottomPanel.setOpaque(false);
            centerPanel.setOpaque(false);
            panelall.setOpaque(false);
            // Panel for the bottom section (Result)

            bottomPanel.add(resultatLabel);
            panel.add(labil);
            panel.add(startButton);
            panel.add(startButton2);
            container.add(startpanel, "start");
            container.add(panel, "panel");
            container.add(panelall, "panelall");
            container.setOpaque(false);

            q.add(container);

            // Ajout du label et du chronomètre seulement pour le niveau difficile

            // Gestionnaire d'événements pour les boutons de choix
            for (int i = 0; i < 4; i++) {
                final int choix = i;
                choixButtons[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        verifierReponse(choix);
                    }
                });
            }

            choicesPanel.setVisible(true);

            choicesPanel.setOpaque(false);
            // Initialisation du jeu

            // Affichage de la fenêtre

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    public void initialiserJeu() {
        if (niveau == 2) {

            topPanel.add(tempsRestantLabel);
            startChrono();
        }

        if (numQuestion >= 10) {
            // Afficher un message de fin du jeu
            replay();
        }

        // Incrémenter le numéro de la question
        numQuestion++;

        // Génération des nombres et de l'opérateur en fonction du niveau
        Random random = new Random();
        if (niveau == 1) {
            nombre1 = random.nextInt(10);
            nombre2 = random.nextInt(10);
            operateur = (random.nextInt(2) == 0) ? '+' : '-';
        } else {
            nombre1 = random.nextInt(10);
            nombre2 = random.nextInt(9) + 1;
            operateur = getOperateur(random.nextInt(4));
            tempsRestant = 10; // Changer la valeur du temps pour le niveau difficile
            tempsRestantLabel.setText("Temps restant: " + tempsRestant + "s");

        }

        // Calcul de la réponse correcte
        reponseCorrecte = calculerReponse();

        // Affichage de la question
        questionLabel.setText(
                "Question " + numQuestion + ": Combien font " + nombre1 + " " + operateur + " " + nombre2 + "?");

        // Génération de choix aléatoires pour les boutons
        int reponseCorrecteIndex = random.nextInt(4);
        for (int i = 0; i < 4; i++) {
            if (i == reponseCorrecteIndex) {
                choixButtons[i].setText(String.valueOf(reponseCorrecte));
            } else {
                choixButtons[i].setText(String.valueOf(genererChoixAleatoire()));
            }
        }

        // Mise à jour de l'affichage du numéro de la question et du score
        scoreLabel.setText("Score: " + score);
        ((JLabel) topPanel.getComponent(0)).setText("Question: " + numQuestion);
    }

    private void verifierReponse(int choix) {
        if (choixButtons[choix].getText().equals(String.valueOf(reponseCorrecte))) {
            resultatLabel.setText("Correct!");
            playSound("Sounds/mixkit-correct-answer-notification-947.wav",
                    correct);

            score++;
            stopSound(timer);
        } else {
            resultatLabel.setText("Incorrect. La réponse correcte est : " + reponseCorrecte);
            playSound("Sounds/mixkit-click-error-1110.wav", error);
            stopSound(timer);
        }

        stopChrono();
        stopSound(timer);
        initialiserJeu();
    }

    private int calculerReponse() {
        switch (operateur) {
            case '+':
                return nombre1 + nombre2;
            case '-':
                return nombre1 - nombre2;
            case '*':
                return nombre1 * nombre2;
            case '/':
                return nombre1 / nombre2;
            default:
                throw new IllegalArgumentException("Opérateur non pris en charge: " + operateur);
        }
    }

    private char getOperateur(int index) {
        char[] operateurs = { '+', '-', '*', '/' };
        return operateurs[index];
    }

    private int genererChoixAleatoire() {
        Random random = new Random();
        return random.nextInt(20); // Modifiez la limite selon vos besoins
    }

    public void startChrono() {
        chrono.start();
    }

    private void stopChrono() {
        if (chrono != null) {
            chrono.stop();
        }
    }

    public static int stopSound(Clip ssd) {
        if (ssd == null) {
            return 0;
        }
        ssd.stop();
        return 0;
    }

    private void replay() {
        Panel replay = new Panel();

        replay.setLayout(null);
        File fontFile = new File("Fonts/Playtime.otf");

        try {
            Font playf = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(20f);
            Button replayButton = new Button();
            replayButton.setText("Rejouer");

            replayButton.setBackground(new Color(252, 244, 200));
            replayButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
            replayButton.setForeground(new Color(114, 69, 45));
            replayButton.setBounds(220, 300, 150, 60);

            Button exitButton = new Button();
            exitButton.setText("Quitter");
            exitButton.setFont(playf);

            exitButton.setBackground(new Color(252, 244, 200));
            exitButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
            exitButton.setForeground(new Color(114, 69, 45));
            exitButton.setBounds(400, 300, 150, 60);
            if (score < 5) {
                JLabel loss = new JLabel("FIN DE JEU VOUS AVEZ PERDU VOTRE SCORE: " + score);
                loss.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
                loss.setForeground(Color.BLACK);
                loss.setBounds(150, 0, 600, 500);
                replay.add(loss);

            } else {
                JLabel win = new JLabel("FIN DE JEU VOUS AVEZ GAGNER VOTRE SCORE: " + score);
                win.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
                win.setForeground(Color.BLACK);
                win.setBounds(150, 0, 600, 500);
                replay.add(win);
            }
            replay.add(replayButton);
            replay.add(exitButton);
            container.add(replay, "replay");
            CardLayout.show(container, "replay");

            replayButton.addActionListener(e -> {
                CardLayout.show(container, "panel");
                score = 0;
                numQuestion = 1;
                ingame = false;
                initialiserJeu();

            });
            exitButton.addActionListener(e -> {
                System.exit(0);
            });

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    public static Clip playSound(String soundFilePath, Clip sd) {
        try {
            sd = AudioSystem.getClip();

            sd.open(AudioSystem.getAudioInputStream(new File(soundFilePath)));
            sd.start();
            return sd;
        } catch (Exception e) {
            e.printStackTrace();
            return sd;

        }

    }

    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////
    // start panel
    public JPanel Creatstart() {

        JPanel firstJPanel = new JPanel(new CardLayout());
        Panel2 p = new Panel2();
        p.setLayout(null);
        ImageIcon imageIcon = new ImageIcon(
                "pic/Screenshot_2023-12-23_131722-removebg-preview.png");
        Image image = imageIcon.getImage();
        Image resizedImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon resizedImageIcon = new ImageIcon(resizedImage);

        Button b = new Button();

        b.setText("Commencer");
        b.setBounds(300, 220, 200, 70);
        b.setBackground(new Color(252, 244, 200));
        b.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        b.setForeground(new Color(114, 69, 45));

        b.addActionListener(e -> {
            CardLayout.show(container, "panel");
        });
        JLabel label = new JLabel(resizedImageIcon);
        label.setBounds(284, 190, 50, 50);
        JLabel label1 = new JLabel("JOUANT AVEC LES MATHS");
        label1.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
        label1.setForeground(Color.BLACK);
        label1.setBounds(230, 45, 400, 50);
        ImageIcon imageIcon2 = new ImageIcon(
                "pic/Screenshot_2023-12-23_131102-removebg-preview.png");
        Image image2 = imageIcon2.getImage();
        Image resizedImage2 = image2.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon resizedImageIcon2 = new ImageIcon(resizedImage2);
        JLabel label2 = new JLabel(resizedImageIcon2);
        label2.setBounds(470, 250, 50, 50);

        p.add(label2);
        p.add(label);
        p.add(label1);
        p.add(b);

        firstJPanel.add(p);

        return firstJPanel;

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new quiz("Jeu de Maths"));
    }
}
