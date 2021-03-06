// Generated from br\u005Cufscar\dc\compiladores\analisadorLexico\Gramatica.g4 by ANTLR 4.9.1
package br.ufscar.dc.compiladores.analisadorLexico;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class Gramatica extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PALAVRAS_CHAVE=1, REP_COND=2, TIPO_DECLA=3, OP_LOG=4, NUM_INT=5, NUM_REAL=6, 
		IDENT=7, CADEIA_ERRADA=8, CADEIA=9, COMENTARIO_ERRADO=10, COMENTARIO=11, 
		WS=12, OP_REL=13, OP_ARIT=14, DELIM=15, ABRE=16, FECHA=17, ERRO=18;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"PALAVRAS_CHAVE", "REP_COND", "TIPO_DECLA", "OP_LOG", "NUM_INT", "NUM_REAL", 
			"IDENT", "CADEIA_ERRADA", "CADEIA", "COMENTARIO_ERRADO", "COMENTARIO", 
			"WS", "OP_REL", "OP_ARIT", "DELIM", "ABRE", "FECHA", "ERRO"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PALAVRAS_CHAVE", "REP_COND", "TIPO_DECLA", "OP_LOG", "NUM_INT", 
			"NUM_REAL", "IDENT", "CADEIA_ERRADA", "CADEIA", "COMENTARIO_ERRADO", 
			"COMENTARIO", "WS", "OP_REL", "OP_ARIT", "DELIM", "ABRE", "FECHA", "ERRO"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public Gramatica(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Gramatica.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 10:
			COMENTARIO_action((RuleContext)_localctx, actionIndex);
			break;
		case 11:
			WS_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void COMENTARIO_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			skip();
			break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:
			skip();
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\24\u017f\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2\u00a7\n\2\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00f2"+
		"\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4\u0120\n\4\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\5\5\u0128\n\5\3\6\6\6\u012b\n\6\r\6\16\6\u012c\3\7\6\7\u0130"+
		"\n\7\r\7\16\7\u0131\3\7\3\7\6\7\u0136\n\7\r\7\16\7\u0137\5\7\u013a\n\7"+
		"\3\b\3\b\7\b\u013e\n\b\f\b\16\b\u0141\13\b\3\t\3\t\7\t\u0145\n\t\f\t\16"+
		"\t\u0148\13\t\3\t\3\t\3\n\3\n\7\n\u014e\n\n\f\n\16\n\u0151\13\n\3\n\3"+
		"\n\3\13\3\13\7\13\u0157\n\13\f\13\16\13\u015a\13\13\3\13\3\13\3\f\3\f"+
		"\7\f\u0160\n\f\f\f\16\f\u0163\13\f\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u0174\n\16\3\17\3\17\3\20\3\20"+
		"\3\21\3\21\3\22\3\22\3\23\3\23\2\2\24\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21"+
		"\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\3\2\r\5\2((\60"+
		"\60``\4\2C\\c|\6\2\62;C\\aac|\5\2\f\f\17\17$$\4\2\f\f\17\17\5\2\f\f\17"+
		"\17\177\177\5\2\13\f\17\17\"\"\6\2\'\',-//\61\61\4\2..<<\4\2**]]\4\2+"+
		"+__\2\u01b0\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2"+
		"\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27"+
		"\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2"+
		"\2\2#\3\2\2\2\2%\3\2\2\2\3\u00a6\3\2\2\2\5\u00f1\3\2\2\2\7\u011f\3\2\2"+
		"\2\t\u0127\3\2\2\2\13\u012a\3\2\2\2\r\u012f\3\2\2\2\17\u013b\3\2\2\2\21"+
		"\u0142\3\2\2\2\23\u014b\3\2\2\2\25\u0154\3\2\2\2\27\u015d\3\2\2\2\31\u0167"+
		"\3\2\2\2\33\u0173\3\2\2\2\35\u0175\3\2\2\2\37\u0177\3\2\2\2!\u0179\3\2"+
		"\2\2#\u017b\3\2\2\2%\u017d\3\2\2\2\'(\7c\2\2()\7n\2\2)*\7i\2\2*+\7q\2"+
		"\2+,\7t\2\2,-\7k\2\2-.\7v\2\2./\7o\2\2/\u00a7\7q\2\2\60\61\7f\2\2\61\62"+
		"\7g\2\2\62\63\7e\2\2\63\64\7n\2\2\64\65\7c\2\2\65\66\7t\2\2\66\u00a7\7"+
		"g\2\2\678\7n\2\289\7g\2\29:\7k\2\2:\u00a7\7c\2\2;<\7g\2\2<=\7u\2\2=>\7"+
		"e\2\2>?\7t\2\2?@\7g\2\2@A\7x\2\2A\u00a7\7c\2\2BC\7h\2\2CD\7k\2\2DE\7o"+
		"\2\2EF\7a\2\2FG\7c\2\2GH\7n\2\2HI\7i\2\2IJ\7q\2\2JK\7t\2\2KL\7k\2\2LM"+
		"\7v\2\2MN\7o\2\2N\u00a7\7q\2\2OP\7\60\2\2P\u00a7\7\60\2\2Q\u00a7\t\2\2"+
		"\2RS\7v\2\2ST\7k\2\2TU\7r\2\2U\u00a7\7q\2\2VW\7r\2\2WX\7t\2\2XY\7q\2\2"+
		"YZ\7e\2\2Z[\7g\2\2[\\\7f\2\2\\]\7k\2\2]^\7o\2\2^_\7g\2\2_`\7p\2\2`a\7"+
		"v\2\2a\u00a7\7q\2\2bc\7h\2\2cd\7k\2\2de\7o\2\2ef\7a\2\2fg\7r\2\2gh\7t"+
		"\2\2hi\7q\2\2ij\7e\2\2jk\7g\2\2kl\7f\2\2lm\7k\2\2mn\7o\2\2no\7g\2\2op"+
		"\7p\2\2pq\7v\2\2q\u00a7\7q\2\2rs\7x\2\2st\7c\2\2t\u00a7\7t\2\2uv\7h\2"+
		"\2vw\7w\2\2wx\7p\2\2xy\7e\2\2yz\7c\2\2z\u00a7\7q\2\2{|\7t\2\2|}\7g\2\2"+
		"}~\7v\2\2~\177\7q\2\2\177\u0080\7t\2\2\u0080\u0081\7p\2\2\u0081\u00a7"+
		"\7g\2\2\u0082\u0083\7h\2\2\u0083\u0084\7k\2\2\u0084\u0085\7o\2\2\u0085"+
		"\u0086\7a\2\2\u0086\u0087\7h\2\2\u0087\u0088\7w\2\2\u0088\u0089\7p\2\2"+
		"\u0089\u008a\7e\2\2\u008a\u008b\7c\2\2\u008b\u00a7\7q\2\2\u008c\u008d"+
		"\7e\2\2\u008d\u008e\7q\2\2\u008e\u008f\7p\2\2\u008f\u0090\7u\2\2\u0090"+
		"\u0091\7v\2\2\u0091\u0092\7c\2\2\u0092\u0093\7p\2\2\u0093\u0094\7v\2\2"+
		"\u0094\u00a7\7g\2\2\u0095\u0096\7x\2\2\u0096\u0097\7g\2\2\u0097\u0098"+
		"\7t\2\2\u0098\u0099\7f\2\2\u0099\u009a\7c\2\2\u009a\u009b\7f\2\2\u009b"+
		"\u009c\7g\2\2\u009c\u009d\7k\2\2\u009d\u009e\7t\2\2\u009e\u00a7\7q\2\2"+
		"\u009f\u00a0\7h\2\2\u00a0\u00a1\7c\2\2\u00a1\u00a2\7n\2\2\u00a2\u00a3"+
		"\7u\2\2\u00a3\u00a7\7q\2\2\u00a4\u00a5\7>\2\2\u00a5\u00a7\7/\2\2\u00a6"+
		"\'\3\2\2\2\u00a6\60\3\2\2\2\u00a6\67\3\2\2\2\u00a6;\3\2\2\2\u00a6B\3\2"+
		"\2\2\u00a6O\3\2\2\2\u00a6Q\3\2\2\2\u00a6R\3\2\2\2\u00a6V\3\2\2\2\u00a6"+
		"b\3\2\2\2\u00a6r\3\2\2\2\u00a6u\3\2\2\2\u00a6{\3\2\2\2\u00a6\u0082\3\2"+
		"\2\2\u00a6\u008c\3\2\2\2\u00a6\u0095\3\2\2\2\u00a6\u009f\3\2\2\2\u00a6"+
		"\u00a4\3\2\2\2\u00a7\4\3\2\2\2\u00a8\u00a9\7u\2\2\u00a9\u00f2\7g\2\2\u00aa"+
		"\u00ab\7g\2\2\u00ab\u00ac\7p\2\2\u00ac\u00ad\7v\2\2\u00ad\u00ae\7c\2\2"+
		"\u00ae\u00f2\7q\2\2\u00af\u00b0\7u\2\2\u00b0\u00b1\7g\2\2\u00b1\u00b2"+
		"\7p\2\2\u00b2\u00b3\7c\2\2\u00b3\u00f2\7q\2\2\u00b4\u00b5\7h\2\2\u00b5"+
		"\u00b6\7k\2\2\u00b6\u00b7\7o\2\2\u00b7\u00b8\7a\2\2\u00b8\u00b9\7u\2\2"+
		"\u00b9\u00f2\7g\2\2\u00ba\u00bb\7e\2\2\u00bb\u00bc\7c\2\2\u00bc\u00bd"+
		"\7u\2\2\u00bd\u00f2\7q\2\2\u00be\u00bf\7u\2\2\u00bf\u00c0\7g\2\2\u00c0"+
		"\u00c1\7l\2\2\u00c1\u00f2\7c\2\2\u00c2\u00c3\7h\2\2\u00c3\u00c4\7k\2\2"+
		"\u00c4\u00c5\7o\2\2\u00c5\u00c6\7a\2\2\u00c6\u00c7\7e\2\2\u00c7\u00c8"+
		"\7c\2\2\u00c8\u00c9\7u\2\2\u00c9\u00f2\7q\2\2\u00ca\u00cb\7r\2\2\u00cb"+
		"\u00cc\7c\2\2\u00cc\u00cd\7t\2\2\u00cd\u00f2\7c\2\2\u00ce\u00cf\7c\2\2"+
		"\u00cf\u00d0\7v\2\2\u00d0\u00f2\7g\2\2\u00d1\u00d2\7h\2\2\u00d2\u00d3"+
		"\7c\2\2\u00d3\u00d4\7e\2\2\u00d4\u00f2\7c\2\2\u00d5\u00d6\7h\2\2\u00d6"+
		"\u00d7\7k\2\2\u00d7\u00d8\7o\2\2\u00d8\u00d9\7a\2\2\u00d9\u00da\7r\2\2"+
		"\u00da\u00db\7c\2\2\u00db\u00dc\7t\2\2\u00dc\u00f2\7c\2\2\u00dd\u00de"+
		"\7g\2\2\u00de\u00df\7p\2\2\u00df\u00e0\7s\2\2\u00e0\u00e1\7w\2\2\u00e1"+
		"\u00e2\7c\2\2\u00e2\u00e3\7p\2\2\u00e3\u00e4\7v\2\2\u00e4\u00f2\7q\2\2"+
		"\u00e5\u00e6\7h\2\2\u00e6\u00e7\7k\2\2\u00e7\u00e8\7o\2\2\u00e8\u00e9"+
		"\7a\2\2\u00e9\u00ea\7g\2\2\u00ea\u00eb\7p\2\2\u00eb\u00ec\7s\2\2\u00ec"+
		"\u00ed\7w\2\2\u00ed\u00ee\7c\2\2\u00ee\u00ef\7p\2\2\u00ef\u00f0\7v\2\2"+
		"\u00f0\u00f2\7q\2\2\u00f1\u00a8\3\2\2\2\u00f1\u00aa\3\2\2\2\u00f1\u00af"+
		"\3\2\2\2\u00f1\u00b4\3\2\2\2\u00f1\u00ba\3\2\2\2\u00f1\u00be\3\2\2\2\u00f1"+
		"\u00c2\3\2\2\2\u00f1\u00ca\3\2\2\2\u00f1\u00ce\3\2\2\2\u00f1\u00d1\3\2"+
		"\2\2\u00f1\u00d5\3\2\2\2\u00f1\u00dd\3\2\2\2\u00f1\u00e5\3\2\2\2\u00f2"+
		"\6\3\2\2\2\u00f3\u00f4\7n\2\2\u00f4\u00f5\7k\2\2\u00f5\u00f6\7v\2\2\u00f6"+
		"\u00f7\7g\2\2\u00f7\u00f8\7t\2\2\u00f8\u00f9\7c\2\2\u00f9\u0120\7n\2\2"+
		"\u00fa\u00fb\7k\2\2\u00fb\u00fc\7p\2\2\u00fc\u00fd\7v\2\2\u00fd\u00fe"+
		"\7g\2\2\u00fe\u00ff\7k\2\2\u00ff\u0100\7t\2\2\u0100\u0120\7q\2\2\u0101"+
		"\u0102\7t\2\2\u0102\u0103\7g\2\2\u0103\u0104\7c\2\2\u0104\u0120\7n\2\2"+
		"\u0105\u0106\7n\2\2\u0106\u0107\7q\2\2\u0107\u0108\7i\2\2\u0108\u0109"+
		"\7k\2\2\u0109\u010a\7e\2\2\u010a\u0120\7q\2\2\u010b\u010c\7t\2\2\u010c"+
		"\u010d\7g\2\2\u010d\u010e\7i\2\2\u010e\u010f\7k\2\2\u010f\u0110\7u\2\2"+
		"\u0110\u0111\7v\2\2\u0111\u0112\7t\2\2\u0112\u0120\7q\2\2\u0113\u0114"+
		"\7h\2\2\u0114\u0115\7k\2\2\u0115\u0116\7o\2\2\u0116\u0117\7a\2\2\u0117"+
		"\u0118\7t\2\2\u0118\u0119\7g\2\2\u0119\u011a\7i\2\2\u011a\u011b\7k\2\2"+
		"\u011b\u011c\7u\2\2\u011c\u011d\7v\2\2\u011d\u011e\7t\2\2\u011e\u0120"+
		"\7q\2\2\u011f\u00f3\3\2\2\2\u011f\u00fa\3\2\2\2\u011f\u0101\3\2\2\2\u011f"+
		"\u0105\3\2\2\2\u011f\u010b\3\2\2\2\u011f\u0113\3\2\2\2\u0120\b\3\2\2\2"+
		"\u0121\u0128\7g\2\2\u0122\u0123\7q\2\2\u0123\u0128\7w\2\2\u0124\u0125"+
		"\7p\2\2\u0125\u0126\7c\2\2\u0126\u0128\7q\2\2\u0127\u0121\3\2\2\2\u0127"+
		"\u0122\3\2\2\2\u0127\u0124\3\2\2\2\u0128\n\3\2\2\2\u0129\u012b\4\62;\2"+
		"\u012a\u0129\3\2\2\2\u012b\u012c\3\2\2\2\u012c\u012a\3\2\2\2\u012c\u012d"+
		"\3\2\2\2\u012d\f\3\2\2\2\u012e\u0130\4\62;\2\u012f\u012e\3\2\2\2\u0130"+
		"\u0131\3\2\2\2\u0131\u012f\3\2\2\2\u0131\u0132\3\2\2\2\u0132\u0139\3\2"+
		"\2\2\u0133\u0135\7\60\2\2\u0134\u0136\4\62;\2\u0135\u0134\3\2\2\2\u0136"+
		"\u0137\3\2\2\2\u0137\u0135\3\2\2\2\u0137\u0138\3\2\2\2\u0138\u013a\3\2"+
		"\2\2\u0139\u0133\3\2\2\2\u0139\u013a\3\2\2\2\u013a\16\3\2\2\2\u013b\u013f"+
		"\t\3\2\2\u013c\u013e\t\4\2\2\u013d\u013c\3\2\2\2\u013e\u0141\3\2\2\2\u013f"+
		"\u013d\3\2\2\2\u013f\u0140\3\2\2\2\u0140\20\3\2\2\2\u0141\u013f\3\2\2"+
		"\2\u0142\u0146\7$\2\2\u0143\u0145\n\5\2\2\u0144\u0143\3\2\2\2\u0145\u0148"+
		"\3\2\2\2\u0146\u0144\3\2\2\2\u0146\u0147\3\2\2\2\u0147\u0149\3\2\2\2\u0148"+
		"\u0146\3\2\2\2\u0149\u014a\t\6\2\2\u014a\22\3\2\2\2\u014b\u014f\7$\2\2"+
		"\u014c\u014e\n\5\2\2\u014d\u014c\3\2\2\2\u014e\u0151\3\2\2\2\u014f\u014d"+
		"\3\2\2\2\u014f\u0150\3\2\2\2\u0150\u0152\3\2\2\2\u0151\u014f\3\2\2\2\u0152"+
		"\u0153\7$\2\2\u0153\24\3\2\2\2\u0154\u0158\7}\2\2\u0155\u0157\n\7\2\2"+
		"\u0156\u0155\3\2\2\2\u0157\u015a\3\2\2\2\u0158\u0156\3\2\2\2\u0158\u0159"+
		"\3\2\2\2\u0159\u015b\3\2\2\2\u015a\u0158\3\2\2\2\u015b\u015c\t\6\2\2\u015c"+
		"\26\3\2\2\2\u015d\u0161\7}\2\2\u015e\u0160\n\7\2\2\u015f\u015e\3\2\2\2"+
		"\u0160\u0163\3\2\2\2\u0161\u015f\3\2\2\2\u0161\u0162\3\2\2\2\u0162\u0164"+
		"\3\2\2\2\u0163\u0161\3\2\2\2\u0164\u0165\7\177\2\2\u0165\u0166\b\f\2\2"+
		"\u0166\30\3\2\2\2\u0167\u0168\t\b\2\2\u0168\u0169\b\r\3\2\u0169\32\3\2"+
		"\2\2\u016a\u0174\7@\2\2\u016b\u016c\7@\2\2\u016c\u0174\7?\2\2\u016d\u0174"+
		"\7>\2\2\u016e\u016f\7>\2\2\u016f\u0174\7?\2\2\u0170\u0171\7>\2\2\u0171"+
		"\u0174\7@\2\2\u0172\u0174\7?\2\2\u0173\u016a\3\2\2\2\u0173\u016b\3\2\2"+
		"\2\u0173\u016d\3\2\2\2\u0173\u016e\3\2\2\2\u0173\u0170\3\2\2\2\u0173\u0172"+
		"\3\2\2\2\u0174\34\3\2\2\2\u0175\u0176\t\t\2\2\u0176\36\3\2\2\2\u0177\u0178"+
		"\t\n\2\2\u0178 \3\2\2\2\u0179\u017a\t\13\2\2\u017a\"\3\2\2\2\u017b\u017c"+
		"\t\f\2\2\u017c$\3\2\2\2\u017d\u017e\13\2\2\2\u017e&\3\2\2\2\21\2\u00a6"+
		"\u00f1\u011f\u0127\u012c\u0131\u0137\u0139\u013f\u0146\u014f\u0158\u0161"+
		"\u0173\4\3\f\2\3\r\3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}