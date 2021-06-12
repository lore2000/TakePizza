package it.uninsubria.takepizza

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val LOCATION_PERMISSION_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLocationAccess()

        addMarker()
        optionMenu()

    }

    fun optionMenu()
    {
        // adding on click listener to marker of google maps.
        mMap.setOnMarkerClickListener { marker -> // on marker click we are getting the title of our marker
            val colors2 = arrayOf("Aggiungi recensione", "Naviga", "Chiudi")

            val builder = AlertDialog.Builder(this@MapsActivity)
            builder.setTitle(marker.title)
            builder.setItems(colors2) { dialog, which ->
                // the user clicked on colors[which]
                if(which == 0){
                    val intent = Intent(this, Recensione::class.java)
                    intent.putExtra("title", marker.title + "")
                    startActivity(intent)

                }
                if(which == 1){

                    val latitudine = marker.position.latitude.toString()
                    val longitudine = marker.position.longitude.toString()

                    goTo(latitudine,longitudine)
                }

            }
            builder.show()

            false
        }
    }

    fun addMarker()
    {
        val positionDante = LatLng(45.5453829, 9.1043588)
        val dante = MarkerOptions().title("Pizzeria Dante").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
            .position(positionDante)
        mMap.addMarker(dante)

        val positionSnoopy = LatLng(45.5499098, 9.1124048)
        val snoopy = MarkerOptions().title("Snoopy").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
            .position(positionSnoopy)
        mMap.addMarker(snoopy)

        val positionGino = LatLng(45.5149185, 9.0524166)
        val gino = MarkerOptions().title("Da Gino").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
            .position(positionGino)
        mMap.addMarker(gino)

        val positionNapuleE = LatLng(45.6840936, 8.924219)
        val napuleE = MarkerOptions().title("napuleE").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
            .position(positionNapuleE)
        mMap.addMarker(napuleE)

        val positionPiedigrotta = LatLng(45.6858542, 8.7077152)
        val piedigrotta  = MarkerOptions().title("Piedigrotta").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
            .position(positionPiedigrotta)
        mMap.addMarker(piedigrotta)
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.buttonStar -> try {
                val intent = Intent(this, Recensione::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            R.id.buttonAccount -> try {
                val intent = Intent(this, Account::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions

                return
            }
            mMap.isMyLocationEnabled = true
            //goToCurrent()
        }
        else
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                mMap.isMyLocationEnabled = true

                goToCurrent()

            }
            else {
                Toast.makeText(this, "L'applicazione non dispone dei permessi necessari", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun goToCurrent()
    {


        val locationManager: LocationManager
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location: Location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
        if (location != null) {
            var currentLocation = LatLng(location.getLatitude(), location.getLongitude())
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f))
        } else {
            Toast.makeText(this, "Segnale GPS assente", Toast.LENGTH_LONG).show()
        }


    }

    @SuppressLint("MissingPermission")
    fun goTo(latitudine: String,longitudine: String) {

        val locationManager: LocationManager
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location: Location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!


        val intent = Intent(Intent.ACTION_VIEW,
            Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d" +
                    "&saddr=" + location.getLatitude() + "," + location.getLongitude() + "&daddr="+ latitudine + "," + longitudine +"&hl=zh&t=m&dirflg=d"))

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        startActivity(intent)

    }


}