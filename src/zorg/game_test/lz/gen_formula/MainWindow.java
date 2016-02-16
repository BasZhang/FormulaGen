package zorg.game_test.lz.gen_formula;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import zorg.gen.formula.GenFormula;

public class MainWindow {

	private JFrame frame;
	private final Action action = new SwingAction();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 448, 244);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(
				new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("282px:grow"),
				ColumnSpec.decode("135px"),},
			new RowSpec[] {
				RowSpec.decode("89px:grow"),
				RowSpec.decode("41px"),}));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, "1, 1, 2, 1, fill, fill");
		
		JLabel label = new JLabel("可使用命令行执行");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(label);
		
		JLabel lblDlzfolderformulagenjavaclasspathgenformulajar = new JLabel("java -cp \"GenFormula.jar\" zorg.gen.formula.GenFormula");
		lblDlzfolderformulagenjavaclasspathgenformulajar.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblDlzfolderformulagenjavaclasspathgenformulajar);
		
		JLabel lbljavacpgenformulajar = new JLabel("\"gen.package\" \"GenClass\" \"输出文件夹/\" \"输入.xlsx\"");
		lbljavacpgenformulajar.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lbljavacpgenformulajar);

		JButton gen = new JButton("GEN~!");
		gen.setAction(action);
		frame.getContentPane().add(gen, "2, 2, center, center");
	}

	private class SwingAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public SwingAction() {
			putValue(NAME, "Gen~!");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
			String[] args = { "zorg.gen.formula", "Formulas",
					"./gen/", "./Formulas.xlsx" };
			try {
				GenFormula.main(args);
			} catch (Exception ex) {
				System.exit(1);
			}
			System.exit(0);
		}
	}
}
