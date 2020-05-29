package logic;

import commands.OutOfBoundsException;
import io.Parse;
import server.Server;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * This class realizing methods for commands.
 */
public class CollectionManager implements Serializable {
    private ArrayList<Route> route;
    private java.time.ZonedDateTime date;

    public CollectionManager() {
        date = java.time.ZonedDateTime.now();
        route = new ArrayList<>();
    }

    /**
     * Method "info" which displays short instruction of every command program.
     */
    public String help() {
        return ("info: вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)" +
                "\nshow: вывести в стандартный поток вывода все элементы коллекции в строковом представлении" +
                "\nadd {element}: добавить новый элемент в коллекцию" +
                "\nupdate_id {element}: обновить значение элемента коллекции, id которого равен заданному" +
                "\nremove_by_id id: удалить элемент из коллекции по его id" +
                "\nclear: очистить коллекцию" +
                "\nsave: сохранить коллекцию в файл" +
                "\nload: загрузить коллекцию из файла" +
                "\nexecute_script file_name: считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме. + " +
                "\nexit: завершить программу (без сохранения в файл)" +
                "\nremove_at index: удалить элемент, находящийся в заданной позиции коллекции (index)" +
                "\nadd_if_max {element}: добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции" +
                "\nadd_if_min {element}: добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции" +
                "\ncount_by_distance distance: вывести количество элементов, значение поля distance которых равно заданному" +
                "\nprint_unique_distance distance: вывести уникальные значения поля distance" +
                "\nprint_field_ascending_distance distance: вывести значение поля distance в порядке возрастания");
    }

    /**
     * This method print info about collection.
     */
    public String info() {
        try {
            Field arrayListField = CollectionManager.class.getDeclaredField("route");
            String arrayListType = arrayListField.getGenericType().getTypeName();
            String[] className = arrayListType.replace("<", " ").
                    replace(">", " ").split("[ .]");
            return ("Type: "  + className[4] +   // className[5]
                    ", initializing date: " + date +
                    ", collection size: " + route.size());
        } catch (NoSuchFieldException e) {
            return ("Problem with general class. Cant find type of class!");
        }
    }

    /**
     * This method shows a elements in collection.
     */
    public String show() {
        if (route.isEmpty()) return ("Collection is empty.");
        else {
            String str = route.stream()
                    .map(Route::toString)
                    .collect(Collectors.joining(("\n")));
            return str;
        }
    }

    /**
     * This method add's a new element to collection.
     * bounds for coordinates and location class.
     */
    public String add(Route newRoute) {
        if (newRoute != null) {
            route.add(newRoute);
            route.get(route.size() - 1).setId();

            return  ("Element was added!");
        }
        return ("Element is null!");
    }

    /**
     * This method update's an element in collection by id.
     * @param id - id of element which we want to update.
     */
    public String update_id(Integer id, Route newElement) {
            try {
                Route oldElement = route.get(getIndexById(id));
                if (newElement != null) {
                    oldElement.setName(newElement.getName());
                    oldElement.setCoordinates(newElement.getCoordinates());
                    oldElement.setFrom(newElement.getFrom());
                    oldElement.setTo(newElement.getTo());
                    oldElement.setDistance(newElement.getDistance());
                    return ("Element with " + id + " was updated!");
                }
            } catch (Exception e) {
                return ("No element with such id!");
            }
            return null;
        }

    /**
     * This method remove's element from collection by id.
     * @param id - argument from console.
     */
    public String remove_by_id(Integer id) {
        try {
            route = route.stream()
                    .filter(Route -> Route.getId() != id)
                    .collect(Collectors.toCollection(ArrayList::new));
            return ("Element with " + id + " was removed!");
        } catch (Exception e) {
            return ("No element with such id!");
        }
    }

    /**
     * This method clear's collection (deleting all elements).
     */
    public String clear() {
        setInitDate(java.time.ZonedDateTime.now());
        route.clear();
        return ("Collection was cleared!");
    }

    /**
     * This method save collection to file.
     */
    public void save(String path) {
        try {
            Parse.parseToJSON(path, route, date);
        } catch (IOException e) {
            System.err.println(Server.parseIOException(e));
        } catch (InvalidPathException e) {
            System.err.println("Invalid path!");
        } catch (NoSuchFieldException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     *
     * @param path - path to collection file.
     */
    public void load(String path) {
        ArrayList<Route> copyOfRoute = new ArrayList<>();
        try {
            copyOfRoute.addAll(route);
            route.clear();
            Route.resetId();

            route.addAll(Parse.parseFromJSON(path));
            setInitDate(Parse.getInitDate(path));
        } catch (IOException e) {
            System.err.println(Server.parseIOException(e));
            route.addAll(copyOfRoute);
        } catch (OutOfBoundsException e) {
            System.err.println("Element from collection file with incorrect fields!");
            route.addAll(copyOfRoute);
        } catch (NullPointerException e) {
            System.err.println("No initialization date in JSON collection file!");
            route.addAll(copyOfRoute);
        } catch (NumberFormatException e) {
            System.err.println("Bad argument of field in json file! \nCorrect your collection.\n");
            route.addAll(copyOfRoute);
        } catch (InvalidPathException e) {
            System.err.println("Invalid path!");
            route.addAll(copyOfRoute);
        }
    }

    /**
     * This method delete element from collection by index.
     * @param index - argument from console.
     */
    public String remove_at(Integer index) {
        try {
            int idx = index;
            route.remove(idx);
            return ("Element with index " + index + " was deleted.");
        } catch (Exception e) {
            return ("No element with such index!");
        }
    }

    /**
     * This method will add new element, if distance of new element is maximal in collection.
     */
    public String add_if_max(Route newRoute)  {
        float maxDistance = route.stream()
                .max(Comparator.comparing(Route::getDistance))
                .get().getDistance();

        if((newRoute != null) && (newRoute.getDistance() > maxDistance)) {
            route.add(newRoute);
            route.get(route.size() - 1).setId();

            return ("New element was added");
        }
        else if (newRoute != null) return ("This distance not maximal in collection");
        return null;
    }

    /**
     * This method will add new element, if distance of new element is minimal in collection.
     */
    public String add_if_min(Route newRoute) {
        float minDistance = route.stream()
                .min(Comparator.comparing(Route::getDistance))
                .get().getDistance();

        if ((newRoute != null) && (newRoute.getDistance() < minDistance)) {
            route.add(newRoute);
            route.get(route.size() - 1).setId();

            return ("New element was added");
        }
        else if (newRoute != null) return ("This distance not minimal in collection");
        return null;
    }

    /**
     * This method returns number of matches with distance.
     * @param distance - argument from console.
     */
    public String count_by_distance(Float distance) {
        try {
            return ("Number of coincidences: " + route.stream()
                    .map(Route::getDistance)
                    .filter(dist -> dist.equals(distance))
                    .count());

        } catch (NumberFormatException e) {
            return ("Bad type of argument!");
        }
    }

    /**
     * This method prints unique values of distances from collection.
     */
    public String print_unique_distance() {
        HashSet<Float> floatHashSet = route.stream()
                .sorted(Route::compareTo)
                .map(Route::getDistance)
                .collect(Collectors.toCollection(HashSet::new));

        return ("Unique distance: " + floatHashSet.toString());
    }

    /**
     * This method prints sorted collection in ascending by distance field.
     */
    public String print_field_ascending_distance() {
        ArrayList<Route> sortedRoute = route.stream()
                .sorted(Comparator.comparing(Route::getDistance))
                .collect(Collectors.toCollection(ArrayList::new));

        String str = "Sorted by distance: [";
        for (int i = 0; i < sortedRoute.size(); i++) {
            Route value = sortedRoute.get(i);
            str += value.getDistance();
            if (i + 1 < sortedRoute.size()) str += (", ");
        }

        return str + "]";
    }

    public String execute_script() {
        return "A new script was started to execute.";
    }



    /**
     * This method returning index of element in collection with id as parameter.
     * @param id - field of element.
     * @return - returns index.
     * @throws Exception - throws exception, if no elements with id from parameter.
     */
    private int getIndexById(int id) throws Exception {
        for (int i = 0; i < route.size(); i++) {
            if (route.get(i).getId() == id) {
                return i;
            }
        } throw new Exception("No such id");
    }

    private void setInitDate(java.time.ZonedDateTime date) {
        this.date = date;
    }
}