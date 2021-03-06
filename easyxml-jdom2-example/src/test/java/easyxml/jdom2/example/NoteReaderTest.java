package easyxml.jdom2.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jdom2.Element;
import org.junit.Test;

import easyxml.jdom2.example.domain.Note;
import easyxml.jdom2.reader.JDom2ItemReaderBuilder;
import easyxml.jdom2.reader.JDom2ReaderBuilder;
import easyxml.reader.Parser;

public class NoteReaderTest {

	@Test
	public void test_with_external_item_reader_class() throws Exception {

		try (InputStream inputStream = new FileInputStream(new File("src/main/resources/note.xml"))) {
			Parser<Element, Note> parser = JDom2ReaderBuilder
				.<Note> reader()
				.addItemReader(new NoteItemReader())
				.setInputStream(inputStream)
				.build();

			for (Note note : parser) {
				System.out.println(
					ToStringBuilder.reflectionToString(note, ToStringStyle.SHORT_PREFIX_STYLE));
			}
		}
	}

	@Test
	public void test_with_two_different_paths() throws Exception {

		try (InputStream inputStream = new FileInputStream(new File("src/main/resources/note.xml"))) {
			Parser<Element, Note> parser = JDom2ReaderBuilder
				.<Note> reader()
				.setInputStream(inputStream)
				.addItemReader(
					JDom2ItemReaderBuilder
						.<Note> itemReader()
						.shouldHandle((p) -> p.getPath().equals("notes/note"))
						.withFunction((e) -> {
							return parseNote(e);
						}).build())
				.addItemReader(
					JDom2ItemReaderBuilder.<Note> itemReader()
						.shouldHandle("notes/specialNote")
						.withFunction((e) -> {
							return parseNote(e);
						}).build())
				.build();

			for (Note note : parser) {
				System.out.println(ToStringBuilder.reflectionToString(note,
					ToStringStyle.SHORT_PREFIX_STYLE));
			}
		}
	}

	private Note parseNote(Element e) {
		Note note = new Note();
		note.setId(Long.valueOf(e.getChildTextTrim("id")));
		note.setContent(e.getChildTextTrim("content"));
		return note;
	}
}
