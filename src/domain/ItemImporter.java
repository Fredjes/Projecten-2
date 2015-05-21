package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import persistence.ItemRepository;

/**
 * Importer for Item-entities that should be extended by an Importer that uses a
 * subclass of Item, since Item is abstract as well.
 *
 * @author Frederik
 * @param <E>
 */
public class ItemImporter<E extends Item> implements Importer {

    private final Set<String> fieldSet = new HashSet<String>(Arrays.asList("", "Leeftijd", "Omschrijving", "Titel", "Thema's"));
    private final List<E> items = new ArrayList<>();
    private Supplier<? extends E> supplier;

    public ItemImporter(Supplier<? extends E> supplier) {
	this.supplier = supplier;
	items.add(supplier.get());
    }

    @Override
    public Set<String> getFields() {
	return fieldSet;
    }

    @Override
    public void initHeaders(List headers) {
    }

    @Override
    public String predictField(String columnName) {
	if (SearchPredicate.containsAnyIgnoreCase(columnName, "leeftijd", "ouderdom")) {
	    return "Leeftijd";
	} else if (SearchPredicate.containsAnyIgnoreCase(columnName, "titel", "naam")) {
	    return "Titel";
	} else if (SearchPredicate.containsAnyIgnoreCase(columnName, "thema", "genre")) {
	    return "Thema's";
	} else {
	    return "Omschrijving"; // default
	}
    }

    @Override
    public void setField(String field, String value) {
	if (!items.isEmpty()) {
	    Item currentItem = items.get(items.size() - 1);
	    switch (field) {
		case "Leeftijd":
		    currentItem.setAgeCategory(value);
		    break;
		case "Omschrijving": {
		    if (currentItem.getDescription() == null || currentItem.getDescription().isEmpty()) {
			currentItem.setDescription(value);
		    } else {
			if (value != null) {
			    currentItem.setDescription(currentItem.getDescription() + (currentItem.getDescription().matches("\\.$") ? " " : ", ") + value);
			}
		    }

		    break;
		}
		case "Titel":
		    currentItem.setName(value);
		    break;
		case "Thema's":
		    currentItem.setThemes(value == null ? new ArrayList<>() : Arrays.asList(value.split("[.,:+-;/]")));
		    break;
	    }
	}
    }

    public E getCurrentEntity() {
	return items.get(items.size() - 1);
    }

    @Override
    public void nextEntity() {
	items.add(supplier.get());
    }

    @Override
    public void persistEntities() {
	items.forEach(ItemRepository.getInstance()::add);
    }
}
