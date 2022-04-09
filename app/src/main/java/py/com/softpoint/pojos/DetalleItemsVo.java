package py.com.softpoint.pojos;


import java.io.Serializable;

public class DetalleItemsVo implements Serializable {

    private String codigoBarras;
    private Long codigoInterno;
    private String descripcion;
    private Double cantidad;

    public DetalleItemsVo(String codigoBarras, Long codigoInterno, String descripcion, Double cantidad) {
        this.codigoBarras = codigoBarras;
        this.codigoInterno = codigoInterno;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }
    public DetalleItemsVo(){

    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public Long getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(Long codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }
}
