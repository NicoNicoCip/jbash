package com.pwstud.jbash.shell.input;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class DefaultJBT {
  public static JBashTerminal getDefault() {
    JBashTerminal term = new JBashTerminal();

    EventListener eventListener = new EventListener() {

    };

    term.eventBus.register(eventListener);
    // Inside your JBashTerminal constructor or reinitTerminalUI
    term.font = new Font("Cascadia Mono", Font.BOLD, 16); // Modern monospaced font

    term.terminalPane.setBackground(new Color(0, 0, 0)); // Dark grey background
    term.terminalPane.setForeground(new Color(255, 255, 255)); // Light grey text
    term.terminalPane.setCaretColor(new Color(255, 255, 255)); // White caret

    term.terminalPane.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(10, 10, 10, 10),
        BorderFactory.createLineBorder(new Color(0, 0, 0), 0, false)));

    JScrollBar verticalScrollBar = term.scrollPane.getVerticalScrollBar();
    verticalScrollBar.setUI(new BasicScrollBarUI() {
      @Override
      protected void configureScrollBarColors() {
        thumbColor = new Color(100, 100, 100);
        thumbDarkShadowColor = thumbColor;
        thumbHighlightColor = thumbColor;
        thumbLightShadowColor = thumbColor;
        trackColor = new Color(0, 0, 0);
        trackHighlightColor = trackColor;
      }

      @Override
      protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
      }

      @Override
      protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
      }

      private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
      }

      @Override
      protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(thumbColor);
        g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 0, 0);
        g2.dispose();
      }

      @Override
      protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(trackColor);
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        g2.dispose();
      }
    });

    term.termColumns = 50;
    term.termRows = 25;
    
    term.updateFontMetrics();
    term.setupFrame();
    return term;
  }
}