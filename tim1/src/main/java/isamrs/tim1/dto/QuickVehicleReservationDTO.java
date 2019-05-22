package isamrs.tim1.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import isamrs.tim1.model.QuickVehicleReservation;

public class QuickVehicleReservationDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -402162839391849498L;
	private String fromDate;
	private String toDate;
	private String vehicleProducer;
	private String vehicleModel;
	private String branchOfficeName;
	private Integer discount;

	public QuickVehicleReservationDTO() {
	}

	public QuickVehicleReservationDTO(QuickVehicleReservation qvr) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		this.fromDate = sdf.format(qvr.getFromDate());
		this.toDate = sdf.format(qvr.getToDate());
		this.vehicleProducer = qvr.getVehicle().getProducer();
		this.vehicleModel = qvr.getVehicle().getModel();
		this.branchOfficeName = qvr.getBranchOffice().getName();
		this.discount = qvr.getDiscount();
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getVehicleProducer() {
		return vehicleProducer;
	}

	public void setVehicleProducer(String vehicleProducer) {
		this.vehicleProducer = vehicleProducer;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public String getBranchOfficeName() {
		return branchOfficeName;
	}

	public void setBranchOfficeName(String branchOfficeName) {
		this.branchOfficeName = branchOfficeName;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

}
