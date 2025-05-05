package com.pwstud.jbash.shell.input;

public abstract class CharList {
  public static final char none = 0;

  // Printable ASCII characters
  public static final char SPACE = 32;
  public static final char EXCLAMATION = 33; // !
  public static final char DOUBLE_QUOTE = 34; // "
  public static final char HASH = 35; // #
  public static final char DOLLAR = 36; // $
  public static final char PERCENT = 37; // %
  public static final char AMPERSAND = 38; // &
  public static final char SINGLE_QUOTE = 39; // '
  public static final char OPEN_PAREN = 40; // (
  public static final char CLOSE_PAREN = 41; // )
  public static final char ASTERISK = 42; // *
  public static final char PLUS = 43; // +
  public static final char COMMA = 44; // ,
  public static final char MINUS = 45; // -
  public static final char DOT = 46; // .
  public static final char SLASH = 47; // /

  public static final char ZERO = 48;
  public static final char ONE = 49;
  public static final char TWO = 50;
  public static final char THREE = 51;
  public static final char FOUR = 52;
  public static final char FIVE = 53;
  public static final char SIX = 54;
  public static final char SEVEN = 55;
  public static final char EIGHT = 56;
  public static final char NINE = 57;

  public static final char COLON = 58; // :
  public static final char SEMICOLON = 59; // ;
  public static final char LESS_THAN = 60; // <
  public static final char EQUAL = 61; // =
  public static final char GREATER_THAN = 62; // >
  public static final char QUESTION = 63; // ?
  public static final char AT = 64; // @

  public static final char A = 65;
  public static final char B = 66;
  public static final char C = 67;
  public static final char D = 68;
  public static final char E = 69;
  public static final char F = 70;
  public static final char G = 71;
  public static final char H = 72;
  public static final char I = 73;
  public static final char J = 74;
  public static final char K = 75;
  public static final char L = 76;
  public static final char M = 77;
  public static final char N = 78;
  public static final char O = 79;
  public static final char P = 80;
  public static final char Q = 81;
  public static final char R = 82;
  public static final char S = 83;
  public static final char T = 84;
  public static final char U = 85;
  public static final char V = 86;
  public static final char W = 87;
  public static final char X = 88;
  public static final char Y = 89;
  public static final char Z = 90;

  public static final char OPEN_BRACKET = 91; // [
  public static final char BACKSLASH = 92; // \
  public static final char CLOSE_BRACKET = 93; // ]
  public static final char CARET = 94; // ^
  public static final char UNDERSCORE = 95; // _
  public static final char BACKTICK = 96; // `

  public static final char a = 97;
  public static final char b = 98;
  public static final char c = 99;
  public static final char d = 100;
  public static final char e = 101;
  public static final char f = 102;
  public static final char g = 103;
  public static final char h = 104;
  public static final char i = 105;
  public static final char j = 106;
  public static final char k = 107;
  public static final char l = 108;
  public static final char m = 109;
  public static final char n = 110;
  public static final char o = 111;
  public static final char p = 112;
  public static final char q = 113;
  public static final char r = 114;
  public static final char s = 115;
  public static final char t = 116;
  public static final char u = 117;
  public static final char v = 118;
  public static final char w = 119;
  public static final char x = 120;
  public static final char y = 121;
  public static final char z = 122;

  public static final char OPEN_BRACE = 123; // {
  public static final char VERTICAL_BAR = 124; // |
  public static final char CLOSE_BRACE = 125; // }
  public static final char TILDE = 126; // ~

  // Control Characters (ASCII 1-31)
  public static final char CTRL_A = 1;
  public static final char CTRL_B = 2;
  public static final char CTRL_C = 3;
  public static final char CTRL_D = 4;
  public static final char CTRL_E = 5;
  public static final char CTRL_F = 6;
  public static final char CTRL_G = 7;
  public static final char BACKSPACE = 8; // Also CTRL + H
  public static final char TAB = 9;
  public static final char NEWLINE = 10; // Line Feed (LF)
  public static final char CTRL_K = 11;
  public static final char CTRL_L = 12;
  public static final char CARRIAGE_RETURN = 13; // Enter (CR)
  public static final char CTRL_N = 14;
  public static final char CTRL_O = 15;
  public static final char CTRL_P = 16;
  public static final char CTRL_Q = 17;
  public static final char CTRL_R = 18;
  public static final char CTRL_S = 19;
  public static final char CTRL_T = 20;
  public static final char CTRL_U = 21;
  public static final char CTRL_V = 22;
  public static final char CTRL_W = 23;
  public static final char CTRL_X = 24;
  public static final char CTRL_Y = 25;
  public static final char CTRL_Z = 26;

  public static final char UP_ARROW = 1000;
  public static final char DOWN_ARROW = 1001;
  public static final char LEFT_ARROW = 1002;
  public static final char RIGHT_ARROW = 1003;
  public static final char SHIFT = 1000;

  public static final char ESC = 27; // ESC key

  public static final char DELETE = 127; // DEL
}
