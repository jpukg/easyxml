package easyxml.jdom2.writer;

import java.util.function.Consumer;
import java.util.function.Function;

import easyxml.jdom2.writer.context.Jdom2WriterContext;

public class Jdom2ItemWriterBuilder {

	protected Function<String, Boolean> shouldHandle;

	protected Consumer<Jdom2WriterContext> consumer;

	public Jdom2ItemWriterBuilder shouldHandle(Function<String, Boolean> shouldHandle) {
		this.shouldHandle = shouldHandle;
		return this;
	}

	public Jdom2ItemWriterBuilder shouldHandle(String path) {
		return this.shouldHandle((p) -> p.equals(path));
	}

	public Jdom2ItemWriterBuilder withFunction(Consumer<Jdom2WriterContext> consumer) {
		this.consumer = consumer;
		return this;
	}

	public Jdom2ItemWriter build() {
		return new Jdom2ItemWriter() {

			@Override
			public void write(Jdom2WriterContext context) {
				consumer.accept(context);
			}

			@Override
			public boolean shouldHandle(String join) {
				return shouldHandle.apply(join);
			}
		};
	}

}
