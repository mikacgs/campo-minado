/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campominadovisual;

import java.awt.event.MouseEvent;

/**
 *
 * @author Michael
 */
public class CampoMinadoVisual {

    public static char campoMinado[][];
    public static Painel painel = new Painel();
    public static int contadorAcertos = 0;
    public static int estados[][] = new int[9][9];

    public static void encheCampo() {
        campoMinado = new char[9][9];

        for (int cont = 0; cont <= 9; cont++) {
            do {
                int linha = (int) (Math.random() * 9);
                int coluna = (int) (Math.random() * 9);
                if (campoMinado[linha][coluna] != '•') {
                    campoMinado[linha][coluna] = '•';
                    break;
                }
            } while (true);
        }
    }

    public static void mapeiaCampo() {
        for (int linha = 0; linha < campoMinado.length; linha++) {
            for (int coluna = 0; coluna < campoMinado.length; coluna++) {
                int contadorMinas = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (linha + i != -1 && coluna + j != -1 && linha + i != 9 && coluna + j != 9) {
                            if (campoMinado[linha + i][coluna + j] == '•') {
                                contadorMinas++;
                            }
                        }
                    }
                }
                if (campoMinado[linha][coluna] != '•') {
                    campoMinado[linha][coluna] = Character.forDigit(contadorMinas, 10);
                }
            }
        }
    }

    public static void zeraCampo() {
        preencheEstados();
        contadorAcertos = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                painel.minas[i][j].setEnabled(true);
                painel.minas[i][j].setText("");
                painel.minas[i][j].setIcon(null);

            }
        }
        preencheEstados();
    }

    public static void exibeTodoCampo() {
        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                String valor = "";
                valor += campoMinado[linha][coluna];
                painel.minas[linha][coluna].setText(valor);
                estados[linha][coluna] = 0;
                if (acertouMina(linha, coluna) == true) {
                    if (painel.minas[linha][coluna].getIcon() == null) {
                        painel.minas[linha][coluna].setIcon(painel.getMina());

                    } else {
                        contadorAcertos++;
                        System.out.println("oi");
                    }
                } else {
                    painel.minas[linha][coluna].setIcon(null);
                }
            }
        }
    }

    public static void novoJogo() {
        zeraCampo();
        encheCampo();
        mapeiaCampo();
    }

    public static void mineSweeper() {
        painel.setVisible(true);
        jogadas();
        novoJogo();
    }

    public static void jogadas() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                final int linha = i;
                final int coluna = j;
                estados[i][j] = 0;
                painel.minas[i][j].addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        if (evt.getButton() == MouseEvent.BUTTON3) {
                            marcaBanderinha(linha, coluna);
                        }
                    }
                });
                painel.minas[i][j].addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        perdeu(linha, coluna);
                    }
                });
            }
        }
        painel.opcoes[0].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                novoJogo();
                painel.desativaFim(true);
            }
        });
        painel.opcoes[1].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zeraCampo();
                painel.desativaFim(true);
            }
        });
        painel.opcoes[2].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.exit(0);
            }
        });
    }

    public static void marcaBanderinha(int i, int j) {
        if (estados[i][j] == 1) {
            painel.minas[i][j].setIcon(painel.getBandeira());
            estados[i][j] = 2;
        } else if (estados[i][j] == 2) {
            painel.minas[i][j].setIcon(null);
            estados[i][j] = 1;
        }
    }

    public static void preencheEstados() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                estados[i][j] = 1;
            }
        }
    }

    public static void abrirVizinhas(int linha, int coluna) {
        if (linha != 9 && linha != -1 && coluna != -1 && coluna != 9) {
            if (campoMinado[linha][coluna] != '0') {
                String valor = "";
                valor += campoMinado[linha][coluna];
                painel.minas[linha][coluna].setText(valor);
                estados[linha][coluna] = 0;
                painel.minas[linha][coluna].setIcon(null);
            } else if (campoMinado[linha][coluna] == '0') {
                if (painel.minas[linha][coluna].getText().equals("")) {
                    painel.minas[linha][coluna].setText("0");
                    estados[linha][coluna] = 0;
                    painel.minas[linha][coluna].setIcon(null);
                    if (linha > 0) {
                        abrirVizinhas(linha - 1, coluna);
                        if (coluna > 0) {
                            abrirVizinhas(linha - 1, coluna - 1);
                        }
                        if (linha < campoMinado.length) {
                            abrirVizinhas(linha - 1, coluna + 1);
                        }
                    }
                    if (linha < campoMinado.length) {
                        abrirVizinhas(linha + 1, coluna);
                        if (coluna > 0) {
                            abrirVizinhas(linha + 1, coluna - 1);
                        }
                        if (coluna < campoMinado[linha].length) {
                            abrirVizinhas(linha + 1, coluna + 1);
                        }
                    }
                    if (coluna > 0) {
                        abrirVizinhas(linha, coluna - 1);
                    }
                    if (coluna < campoMinado[linha].length) {
                        abrirVizinhas(linha, coluna + 1);
                    }
                }
            }
        }
        ganhou();
    }

    public static void perdeu(int i, int j) {
        if (estados[i][j] == 1) {
            if (!acertouMina(i, j)) {
                abrirVizinhas(i, j);
            } else {
                exibeTodoCampo();
                painel.minas[i][j].setEnabled(false);
                painel.desativaFim(false);
                continuar("Perdeu", contadorAcertos);
            }
        }
    }

    public static void continuar(String fim, int acertos) {
        painel.desativaFim(false);
        painel.textos[0].setText("Você " + fim + "!!");
        painel.textos[2].setText("Acertou " + acertos + " minas");
        painel.textos[1].setText("Deseja seguir Jogando ?");
    }

    public static void ganhou() {
        if (escanearVazios()) {
            contadorAcertos = 10;
            continuar("ganhou", contadorAcertos);
        }
    }

    public static boolean escanearVazios() {
        int contaAbertos = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                switch (painel.minas[i][j].getText()) {
                    case "1":
                    case "0":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                    case "6":
                    case "7":
                    case "8":
                        contaAbertos++;
                }
            }

        }
        if (contaAbertos == 71) {
            return true;
        }
        return false;
    }

    public static boolean acertouMina(int i, int j) {
        return campoMinado[i][j] == '•';
    }

    public static void main(String[] args) {
        mineSweeper();
    }

}
