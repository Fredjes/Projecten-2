package domain;

import domain.Item;
import domain.Loan;
import domain.User;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import persistence.ItemRepository;
import persistence.LoanRepository;
import persistence.SettingsManager;
import persistence.UserRepository;

/**
 * A Util-class to export the current state of the system.
 *
 * @author Frederik
 */
public class PdfExporter {

    private static final String USER_FILE_NAME = "Gebruikers";
    private static final String LOAN_FILE_NAME = "Uitleningen";
    private static final String ITEMS_FILE_NAME = "Voorwerpen";
    private static final PDFont FONT = PDType1Font.HELVETICA;
    private static final int X_OFFSET = 50;
    private static final int Y_OFFSET = 50;
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private static final String DIRECTORY = SettingsManager.instance.getString("pdfPath");

    static {
	if (!Files.exists(Paths.get(DIRECTORY))) {
	    try {
		Files.createDirectory(Paths.get(DIRECTORY));
	    } catch (IOException ex) {
	    }
	}
    }

    public static void saveUsers() throws IOException {
	final int stepY = 50;
	PDDocument document = new PDDocument();
	List<User> users = UserRepository.getInstance().getUsersByPredicate(u -> u.getUserType() == User.UserType.STUDENT);
	float y = 0;
	PDPage page;
	PDRectangle rectangle;
	PDPageContentStream cos;

	page = new PDPage(PDPage.PAGE_SIZE_A4);
	document.addPage(page);
	rectangle = page.getMediaBox();
	cos = new PDPageContentStream(document, page);

	cos.beginText();
	cos.moveTextPositionByAmount(X_OFFSET, rectangle.getHeight() - Y_OFFSET - y);
	cos.setFont(FONT, 21);
	cos.drawString("Overzicht van leerlingen");
	y += 40;
	cos.endText();

	for (User user : users) {
	    if (user.getName() == null || user.getName().isEmpty() || user.getName().equals("null")) {
		continue;
	    }

	    if (rectangle.getHeight() - y <= 100) {
		cos.close();
		page = new PDPage(PDPage.PAGE_SIZE_A4);
		document.addPage(page);
		rectangle = page.getMediaBox();
		cos = new PDPageContentStream(document, page);
		y = 0;
	    }

	    cos.beginText();
	    cos.moveTextPositionByAmount(X_OFFSET, rectangle.getHeight() - Y_OFFSET - y);
	    cos.setFont(FONT, 13);
	    if (user.getName() != null) {
		cos.drawString(user.getName() + ", " + user.getClassRoom() + (user.getRegisterNumber() == null || user.getRegisterNumber().isEmpty() ? "" : ", " + user.getRegisterNumber()));
	    }

	    y += stepY;
	    cos.endText();
	}

	cos.close();

	try {
	    document.save(DIRECTORY + "/" + USER_FILE_NAME + " op " + DATE_FORMAT.format(Date.from(Instant.now())) + ".pdf");
	} catch (COSVisitorException ex) {
	    Logger.getLogger(PdfExporter.class.getName()).log(Level.SEVERE, null, ex);
	} finally {
	    document.close();
	}
    }

    public static void saveLoanHistory() {
	try {
	    saveLoans(DIRECTORY + "/Teruggebrachte uitleningen op " + DATE_FORMAT.format(Date.from(Instant.now())) + ".pdf", "Overzicht van gesloten uitleningen", LoanRepository.getInstance().getLoans().stream().filter(l -> l.getReturned()).collect(Collectors.toList()));
	} catch (IOException ex) {
	}
    }

    public static void saveLoans() {
	try {
	    saveLoans(DIRECTORY + "/" + LOAN_FILE_NAME + " op " + DATE_FORMAT.format(Date.from(Instant.now())) + ".pdf", "Overzicht van open uitleningen", LoanRepository.getInstance().getLoans().stream().filter(l -> !l.getReturned()).collect(Collectors.toList()));
	} catch (IOException ex) {
	}
    }

    private static void saveLoans(String filename, String title, List<Loan> loans) throws IOException {
	final int stepY = 50;
	PDDocument document = new PDDocument();
	float y = 0;
	PDPage page;
	PDRectangle rectangle;
	PDPageContentStream cos;

	page = new PDPage(PDPage.PAGE_SIZE_A4);
	document.addPage(page);
	rectangle = page.getMediaBox();
	cos = new PDPageContentStream(document, page);

	cos.beginText();
	cos.moveTextPositionByAmount(X_OFFSET, rectangle.getHeight() - Y_OFFSET - y);
	cos.setFont(FONT, 21);
	cos.drawString(title);
	y += 40;
	cos.endText();

	for (Loan loan : loans) {
	    if (rectangle.getHeight() - y <= 130) {
		cos.close();
		page = new PDPage(PDPage.PAGE_SIZE_A4);
		document.addPage(page);
		rectangle = page.getMediaBox();
		cos = new PDPageContentStream(document, page);
		y = 0;
	    }

	    cos.beginText();
	    cos.moveTextPositionByAmount(X_OFFSET, rectangle.getHeight() - Y_OFFSET - y);
	    cos.setFont(FONT, 13);
	    cos.drawString(loan.toString());
	    cos.moveTextPositionByAmount(20, -15);
	    String toLatetext = loan.getDate().before(Date.from(Instant.now())) && !loan.getReturned() ? "(te laat)" : "";
	    cos.drawString("van " + DATE_FORMAT.format(Date.from(loan.getStartDate().toInstant())) + " tot " + DATE_FORMAT.format(Date.from(loan.getDate().toInstant())) + " " + toLatetext);

	    y += stepY;
	    cos.endText();
	}

	cos.close();

	try {
	    document.save(filename);
	} catch (COSVisitorException ex) {
	    Logger.getLogger(PdfExporter.class.getName()).log(Level.SEVERE, null, ex);
	} finally {
	    document.close();
	}
    }

    public static void saveItems() throws IOException {
	final int stepY = 50;
	PDDocument document = new PDDocument();
	List<? extends Item> items = ItemRepository.getInstance().getItemsByPredicate(Item::getVisible);
	float y = 0;
	PDPage page;
	PDRectangle rectangle;
	PDPageContentStream cos;

	page = new PDPage(PDPage.PAGE_SIZE_A4);
	document.addPage(page);
	rectangle = page.getMediaBox();
	cos = new PDPageContentStream(document, page);

	cos.beginText();
	cos.moveTextPositionByAmount(X_OFFSET, rectangle.getHeight() - Y_OFFSET - y);
	cos.setFont(FONT, 21);
	cos.drawString("Overzicht van open uitleningen");
	y += 40;
	cos.endText();

	for (Item item : items) {
	    if (item.getName() == null || item.getName().isEmpty() || item.getName().equals("null")) {
		continue;
	    }

	    if (rectangle.getHeight() - y <= 100) {
		cos.close();
		page = new PDPage(PDPage.PAGE_SIZE_A4);
		document.addPage(page);
		rectangle = page.getMediaBox();
		cos = new PDPageContentStream(document, page);
		y = 0;
	    }

	    cos.beginText();
	    cos.moveTextPositionByAmount(X_OFFSET, rectangle.getHeight() - Y_OFFSET - y);
	    cos.setFont(FONT, 13);
	    StringBuilder builder = new StringBuilder();
	    item.getItemCopies().forEach(ic -> builder.append(ic.getCopyNumber()).append(", "));

	    cos.drawString(item.getName() + (item.getItemCopies().isEmpty() ? " zonder exemplaren." : (" met exemplaren: " + builder.toString().substring(0, builder.toString().length() - 2))));
	    cos.moveTextPositionByAmount(20, -15);

	    y += stepY;
	    cos.endText();
	}

	cos.close();

	try {
	    document.save(DIRECTORY + "/" + ITEMS_FILE_NAME + " op " + DATE_FORMAT.format(Date.from(Instant.now())) + ".pdf");
	} catch (COSVisitorException ex) {
	    Logger.getLogger(PdfExporter.class.getName()).log(Level.SEVERE, null, ex);
	} finally {
	    document.close();
	}
    }
}
