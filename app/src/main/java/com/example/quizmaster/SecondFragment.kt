import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.quizmaster.databinding.ActivitySecondBinding  // Asegúrate de que el nombre de tu clase de enlace sea correcto

class SecondFragment : Fragment() {

    private lateinit var binding: ActivitySecondBinding  // Asegúrate de que el nombre de tu clase de enlace sea correcto

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivitySecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener datos pasados desde MainActivity y actualizar la interfaz de usuario
        val args = arguments
        if (args != null) {
            val nombre = args.getString("nombre", "")
            val numeroPreguntas = args.getString("numeroPreguntas", "")

            // Llamar a la función para actualizar la interfaz de usuario
            actualizarInterfazUsuario(nombre, numeroPreguntas)
        }
    }

    // Función para actualizar la interfaz de usuario con los datos recibidos
    private fun actualizarInterfazUsuario(nombre: String, numeroPreguntas: String) {
        binding.textViewNombre.text = "Nombre: $nombre"
        binding.textViewNumeroPreguntas.text = "Número de Preguntas: $numeroPreguntas"
    }
}
