package Reflection;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;

public class Utility {
	public static String printObject(Object obj)
	{
		String res = Utility.printObject(obj, "");
		System.out.println(res);
		return res;
	}
	
	private static String printObject(Object obj, String tabs)
	{
		if (obj == null) return "";
		
		String str = "Object of class \"" + obj.getClass().getName() + "\"\n";
		
		int size = str.length() - 1;
		
		str = tabs + str + tabs;
		
		for (int j = 0; j < size; j++) {
			str = str + "-";
		}
		str = str + "\n";
		
		Field[] fields = obj.getClass().getFields();
		
		for (Field f : fields) {
			try {
				if (Utility.isCollection(f.get(obj))) { // collection
					Collection<?> c = (Collection<?>) f.get(obj);
					
					if (c.isEmpty()) {
						str = str + tabs + f.getName() + " = empty \"" + c.getClass().getName() + "\"\n";
						continue;
					}
					
					String tmpStr = "";
					Class<?> cls = Object.class;
					for (Object collObj : c) {
						cls = collObj.getClass();
						tmpStr = tmpStr + Utility.printObject(collObj, tabs + "\t");
					}
					
					str = str + tabs + f.getName() + " = Object of class \"" + c.getClass().getName() + "\" of " + "\"" + cls.getName() + "\" elements\n" + tmpStr;
					
				} else if (Utility.isArray(f.get(obj))) { // array
					str = str + tabs + f.getName() + "[] =\n";
					
					for (int j = 0; j < Array.getLength(f.get(obj)); j++) {
						Object tmp = Array.get(f.get(obj), j);
						str = str + tabs + f.getName() + "[" + j + "] = \n" + Utility.printObject(tmp, tabs + "\t");
					}
					
				} else if (Utility.isPrimitiveOrString(f.get(obj))) { // primitive or String
					str = str + tabs + f.getName() + " = " + f.get(obj) + "\n";					
				} else { // simple "struct"
					str = str + tabs + f.getName() + " =\n" + Utility.printObject(f.get(obj), tabs + "\t");
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return str;
	}
	
	private static boolean isArray(Object object) {
		return (object == null ? false : object.getClass().isArray());
	}

	private static boolean isCollection(Object object) {
		return (object == null ? false : Collection.class.isAssignableFrom(object.getClass()));
	}

	private static boolean isPrimitiveOrString(Object obj) {
		if (obj == null || obj.getClass() == null) {
			return true; 
		}
		
		switch (obj.getClass().getName()) {
		case "java.lang.Double":
		case "java.lang.Integer":
		case "java.lang.Boolean":
		case "java.lang.Byte":
		case "java.lang.Character":
		case "java.lang.Short":
		case "java.lang.Long":
		case "java.lang.Float":
		case "java.lang.String":
			return true;
		default: return false;
		}
	}

	public static void main(String args[])
	{	
		// null check
		Utility.printObject(null);
		
		DataBase db = new DataBase();
		
		// null fields
		Utility.printObject(db);
		
		Name n = new Name();
		
		Utility.printObject(n);
		
		n.firstName = "Aviva";
		n.lastName = "Herman";
		
		// prints a real object
		Utility.printObject(n);
		
		Person p = new Person();
		
		// null fields
		Utility.printObject(p);
		
		p.age = 24;
		p.name = n;
		
		// prints a real object
		Utility.printObject(p);
		
		Address ad = new Address();
		
		ad.phone = "039628532";
		ad.st = new Street();
		ad.st.name = "Ha'Rav Neria St.";
		ad.st.num = 6;
		
		p.address = ad;
		
		// prints nested "structs"
		Utility.printObject(p);
		
		
		// Test collection and array
		db.names = new Person[5];
		db.ids = new LinkedList<>();
		db.num = 0;
		
		for (int i = 0; i < db.names.length; i++) {
			db.num++;
			db.names[i] = new Person();
			db.names[i].age = i + 20;
			db.names[i].name = new Name();
			db.names[i].name.firstName = "Aviva" + i;
			db.names[i].name.lastName = "Herman" + i;
			db.names[i].address = new Address();
			db.names[i].address.phone = "" + (i + 1) * 1000000;
			db.names[i].address.st = new Street();
			db.names[i].address.st.name = "Ha'Rav Neria St.";
			db.names[i].address.st.num = i;
			Name name = new Name();
			name.firstName = "a" + i;
			name.lastName = "b" + i;
			db.ids.addLast(name);
		}
		
		Utility.printObject(db);
	}
}
