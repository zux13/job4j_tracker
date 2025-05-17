package ru.job4j.tracker.input;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.tracker.Item;
import ru.job4j.tracker.MemTracker;
import ru.job4j.tracker.action.*;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.output.StubOutput;

class ConsoleInputTest {

    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final Input INPUT = mock(Input.class);
    private static MemTracker tracker;
    private static Output output;

    @BeforeEach
    public void initializeFields() {
        tracker = new MemTracker();
        output = new StubOutput();
    }

    @Test
    public void whenItemWasReplacedSuccessfully() {

        tracker.add(new Item("Replaced item"));
        String replacedName = "New item name";
        Replace replaceAction = new Replace(output);

        when(INPUT.askInt(any(String.class))).thenReturn(1);
        when(INPUT.askStr(any(String.class))).thenReturn(replacedName);

        replaceAction.execute(INPUT, tracker);

        assertThat(output.toString()).isEqualTo(
                "=== Edit item ===" + LINE_SEPARATOR
                        + "Заявка изменена успешно." + LINE_SEPARATOR
        );
    }

    @Test
    public void whenItemWasDeletedSuccessfully() {

        tracker.add(new Item("First Item"));

        Delete deleteAction = new Delete(output);

        when(INPUT.askInt(any(String.class))).thenReturn(1);

        deleteAction.execute(INPUT, tracker);

        assertThat(output.toString()).isEqualTo(
                "=== Delete item ===" + LINE_SEPARATOR
                        + "Заявка удалена успешно." + LINE_SEPARATOR
        );
    }

    @Test
    public void whenItemDeleteFails() {

        tracker.add(new Item("First Item"));
        tracker.add(new Item("Second Item"));

        Delete deleteAction = new Delete(output);

        when(INPUT.askInt(any(String.class))).thenReturn(3);

        deleteAction.execute(INPUT, tracker);

        assertThat(output.toString()).isEqualTo(
                "=== Delete item ===" + LINE_SEPARATOR
                        + "Ошибка удаления заявки." + LINE_SEPARATOR
        );
    }

    @Test
    public void whenFindByIdSuccessfully() {

        Item itemToFind = new Item("Found Item");
        tracker.add(new Item("First Item"));
        tracker.add(itemToFind);

        FindById findByIdAction = new FindById(output);

        when(INPUT.askInt(any(String.class))).thenReturn(2);

        findByIdAction.execute(INPUT, tracker);

        assertThat(output.toString()).isEqualTo(
                "=== Find item by id ===" + LINE_SEPARATOR
                        + itemToFind + LINE_SEPARATOR
        );
    }

    @Test
    public void whenItemNotFoundById() {

        tracker.add(new Item("First Item"));
        tracker.add(new Item("Second Item"));

        FindById findByIdAction = new FindById(output);

        when(INPUT.askInt(any(String.class))).thenReturn(3);

        findByIdAction.execute(INPUT, tracker);

        assertThat(output.toString()).isEqualTo(
                "=== Find item by id ===" + LINE_SEPARATOR
                        + "Заявка с введенным id: 3 не найдена." + LINE_SEPARATOR
        );
    }

    @Test
    public void whenItemsFoundByNameSuccessfully() {

        String itemName = "Found Item";
        Item firstItem = new Item(itemName);
        Item secondItem = new Item(itemName);

        tracker.add(firstItem);
        tracker.add(secondItem);

        FindByName findByNameAction = new FindByName(output);

        when(INPUT.askStr(any(String.class))).thenReturn(itemName);

        findByNameAction.execute(INPUT, tracker);

        assertThat(output.toString()).isEqualTo(
                "=== Find items by name ===" + LINE_SEPARATOR
                        + firstItem + LINE_SEPARATOR
                        + secondItem + LINE_SEPARATOR
        );
    }

    @Test
    public void whenItemsNotFoundByName() {

        tracker.add(new Item("First Item"));
        tracker.add(new Item("Second Item"));

        FindByName findByNameAction = new FindByName(output);

        String itemName = "Third Item";
        when(INPUT.askStr(any(String.class))).thenReturn(itemName);

        findByNameAction.execute(INPUT, tracker);

        assertThat(output.toString()).isEqualTo(
                "=== Find items by name ===" + LINE_SEPARATOR
                        + "Заявки с именем: " + itemName + " не найдены." + LINE_SEPARATOR
        );
    }
}