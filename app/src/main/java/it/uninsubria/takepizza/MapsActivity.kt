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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLocationAccess()
        val intent = intent


       val positionProva = LatLng(45.560471, 9.112582)
        val prova = MarkerOptions().title("Pizzeria bella Napoli").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
            .position(positionProva)
        mMap.addMarker(prova)

        val positionSnoopy = LatLng(45.5515215, 9.1151543)
        val snoopy = MarkerOptions().title("Snoopy").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                .position(positionSnoopy)
        mMap.addMarker(snoopy)


        // adding on click listener to marker of google maps.
        mMap.setOnMarkerClickListener { marker -> // on marker click we are getting the title of our marker
            val colors2 = arrayOf("Aggiungi recensione", "Naviga", "Info pizzeria", "Chiudi")

            val builder = AlertDialog.Builder(this@MapsActivity)
            builder.setTitle(marker.title)
            builder.setItems(colors2) { dialog, which ->
                // the user clicked on colors[which]
                if(which == 0){
                    //first option clicked, do this...


                }
                if(which == 1){
                    //first option clicked, do this...

                    val latitudine = marker.position.latitude.toString()
                    val longitudine = marker.position.longitude.toString()

                    goTo(latitudine,longitudine)
                }

            }
            builder.show()

            false
        }






    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.buttonStar -> try {
                val intent3 = Intent(this, Recensione::class.java)
                startActivity(intent3)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            R.id.buttonAccount -> try {
                val intent2 = Intent(this, Account::class.java)
                val email = intent.getStringExtra("email")
                intent2.putExtra("email", email + "")
                startActivity(intent2)
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
            goToCurrent()
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