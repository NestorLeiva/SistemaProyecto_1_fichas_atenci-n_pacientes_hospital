// NESTOR LEIVA VILLALOBOS / GRUPO 02 
// PROYECTO PROGRAMADO # 1
// PROF: JAIRO RAMIREZ SOJO
import java.util.Scanner;

// Clase que representa a un paciente en el sistema
class Paciente {
    String nombre; 
    String tipoFicha; // Tipo de servicio: General, Odontologia, Emergencias
    int prioridad; // Nivel de prioridad
    Paciente sig; // Referencia al siguiente paciente en la cola
    Paciente ant; // Referencia al paciente anterior en la cola

    // Constructor que inicializa los atributos del paciente
    public Paciente(String nombre, String tipoFicha, int prioridad) {
        this.nombre = nombre.toUpperCase(); 
        this.tipoFicha = tipoFicha; // Asigna el tipo de ficha
        this.prioridad = prioridad; // Asigna la prioridad
        this.sig = null; // Inicializa el siguiente paciente como nulo
        this.ant = null; // Inicializa el anterior paciente como nulo
    }

    // Metodo para imprimir la informacion del paciente
    public void imprimir() {
        String descripcionPrioridad = obtenerDescripcionPrioridad(prioridad); // Obtiene la descripcion de la prioridad
        System.out.println("NOMBRE: " + nombre + ", TIPO DE FICHA: " + tipoFicha + ", PRIORIDAD: " + descripcionPrioridad);
    }

    // Metodo para obtener la descripcion de la prioridad
    public String obtenerDescripcionPrioridad(int prioridad) { 
        switch (prioridad) {
            case 1: return "MUY BAJA PRIORIDAD";
            case 2: return "BAJA PRIORIDAD";
            case 3: return "MEDIA PRIORIDAD";
            case 4: return "ALTA PRIORIDAD";
            case 5: return "EMERGENCIA";
            default: return "PRIORIDAD NO VALIDA"; // Maneja prioridades no validas
        }
    }
}

// Clase que representa la cola de atencion para los pacientes
class ColaDeAtencion {
    Paciente cabeza = null; // Primer paciente en la cola
    Paciente cola = null; // Ultimo paciente en la cola

    // Metodo para verificar si la cola esta vacia
    public boolean estaVacia() {
        return cabeza == null; // Retorna verdadero si no hay pacientes
    }

    // Metodo para insertar un paciente en la cola segun su prioridad
    public void insertarPaciente(Paciente nuevoPaciente) {
        if (estaVacia()) {
            cabeza = cola = nuevoPaciente; // Si esta vacia, el nuevo paciente es tanto cabeza como cola
        } else {
            Paciente actual = cabeza; 
            while (actual != null && actual.prioridad >= nuevoPaciente.prioridad) { 
                actual = actual.sig; // Busca la posicion correcta segun la prioridad
            }
            if (actual == cabeza) {
                nuevoPaciente.sig = cabeza;
                cabeza.ant = nuevoPaciente;
                cabeza = nuevoPaciente; // Inserta al inicio si tiene mayor prioridad que el primero
            } else if (actual == null) {
                cola.sig = nuevoPaciente;
                nuevoPaciente.ant = cola;
                cola = nuevoPaciente; // Inserta al final si tiene menor prioridad que todos
            } else {
                nuevoPaciente.sig = actual;
                nuevoPaciente.ant = actual.ant;
                actual.ant.sig = nuevoPaciente;
                actual.ant = nuevoPaciente; // Inserta en medio de dos pacientes existentes
            }
        }
    }

    // Metodo para atender al paciente en la cabeza de la cola
    public Paciente atenderPaciente() {
        if (estaVacia()) {
            System.out.println("NO HAY PACIENTES EN ESPERA."); 
            return null; // Mensaje si no hay pacientes
        }
        Paciente atendido = cabeza; 
        cabeza = cabeza.sig; 
        if (cabeza != null) cabeza.ant = null; 
        else cola = null; 
        return atendido; 
    }

    // Metodo para mostrar la lista de pacientes en espera
    public void mostrarPacientes() {
        if (estaVacia()) {
            System.out.println("NO HAY PACIENTES EN ESPERA."); 
        } else {
            Paciente actual = cabeza;
            while (actual != null) {
                actual.imprimir(); 
                actual = actual.sig; 
            }
        }
    }
}

// Clase principal del sistema hospitalario
public class SistemaHospital {
    private ColaDeAtencion[] colas; 

    public SistemaHospital() { 
        colas = new ColaDeAtencion[3]; 
        colas[0] = new ColaDeAtencion(); 
        colas[1] = new ColaDeAtencion(); 
        colas[2] = new ColaDeAtencion(); 
    }

    // Metodo para validar el nombre del paciente
    public boolean validarNombre(String nombre) {
        for (char c : nombre.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
                return false; // Retorna false si encuentra un caracter no valido
            }
        }
        return true; // Retorna true si todos los caracteres son validos
    }

    public void registrarPaciente(String nombre, int tipoFicha, int prioridad) { 
        String tipoConsulta = ""; 
        switch (tipoFicha) { 
            case 1: tipoConsulta = "GENERAL"; break;
            case 2: tipoConsulta = "ODONTOLOGIA"; break;
            case 3: tipoConsulta = "EMERGENCIAS"; break;
            default: System.out.println("TIPO DE FICHA NO VALIDO."); return; 
        } 

        if (!validarNombre(nombre)) {
            System.out.println("NOMBRE INVALIDO. SOLO SE PERMITEN LETRAS.");
            return; // Termina el metodo si el nombre es invalido
        }

        Paciente nuevoPaciente = new Paciente(nombre, tipoConsulta, prioridad); 
        colas[tipoFicha - 1].insertarPaciente(nuevoPaciente); 
        System.out.println("PACIENTE " + nombre.toUpperCase() + " REGISTRADO EN " + tipoConsulta + " CON PRIORIDAD " + nuevoPaciente.obtenerDescripcionPrioridad(prioridad)); 
    }

    public void atenderPaciente(int tipoFicha) { 
        if (tipoFicha < 1 || tipoFicha > 3) { 
            System.out.println("TIPO DE FICHA NO VALIDO."); return; 
        } 

        Paciente atendido = colas[tipoFicha - 1].atenderPaciente(); 
        if (atendido != null) { 
            System.out.println("ATENDIENDO A: " + atendido.nombre); 
        } 
    }

    public void mostrarPacientes(int tipoFicha) { 
        if (tipoFicha < 1 || tipoFicha > 3) { 
            System.out.println("TIPO DE FICHA NO VALIDO."); return; 
        } 

        System.out.println("PACIENTES EN ESPERA PARA " + (tipoFicha == 1 ? "GENERAL" : tipoFicha == 2 ? "ODONTOLOGIA" : "EMERGENCIAS") + ":"); 
        colas[tipoFicha - 1].mostrarPacientes(); 
    }

    public static void main(String[] args) { 
        SistemaHospital hospital = new SistemaHospital(); 
        Scanner scanner = new Scanner(System.in); 

        boolean continuar = true;

        while (continuar) { 
            System.out.println("\n--- SISTEMA DE ATENCION HOSPITALARIA ---"); 
            System.out.println("1. REGISTRAR NUEVO PACIENTE"); 
            System.out.println("2. ATENDER PACIENTE"); 
            System.out.println("3. MOSTRAR LISTA DE PACIENTES"); 
            System.out.println("4. SALIR"); 

            System.out.print("SELECCIONA UNA OPCION: "); 

            try { 
                int opcion = Integer.parseInt(scanner.nextLine()); 

                switch (opcion) { 
                    case 1:
                        System.out.print("NOMBRE DEL PACIENTE: "); 
                        String nombre = scanner.nextLine(); 

                        System.out.print("TIPO DE CONSULTA (1: GENERAL, 2: ODONTOLOGIA, 3: EMERGENCIAS): ");  
                        int tipoFicha = Integer.parseInt(scanner.nextLine()); 

                        System.out.print("NIVEL DE PRIORIDAD (1 A 5): ");  
                        int prioridad = Integer.parseInt(scanner.nextLine()); 

                        if (prioridad < 1 || prioridad > 5) {  
                            System.out.println("PRIORIDAD NO VALIDA. DEBE SER ENTRE 1 Y 5.");  
                        } else {  
                            hospital.registrarPaciente(nombre, tipoFicha, prioridad);  
                        }  
                        break;

                    case 2:
                        System.out.print("TIPO DE CONSULTA A ATENDER (1: GENERAL, 2: ODONTOLOGIA, 3: EMERGENCIAS): ");  
                        int tipoAtencion = Integer.parseInt(scanner.nextLine());  
                        hospital.atenderPaciente(tipoAtencion);  
                        break;

                    case 3:
                        System.out.print("TIPO DE CONSULTA (1: GENERAL, 2: ODONTOLOGIA, 3: EMERGENCIAS): ");  
                        int tipoMostrar = Integer.parseInt(scanner.nextLine());  
                        hospital.mostrarPacientes(tipoMostrar);  
                        break;

                    case 4:
                        continuar = false;  
                        System.out.println("SALIENDO DEL SISTEMA...");  
                        break;

                    default:
                        System.out.println("OPCION NO VALIDA.");  
                        break;
                }  
            } catch (NumberFormatException e) {  
                System.out.println("ERROR: ENTRADA NO VALIDA. POR FAVOR, INGRESA UN NUMERO.");  
            }  
        }  

        scanner.close();  
    }  
}
