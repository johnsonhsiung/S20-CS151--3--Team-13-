package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;

import model.Evaluation;
import model.Medicine;
import model.Patient;
import model.PatientModel;
import model.Visit;
import view.*;

public class PatientController {

	private static JFrame frame;
	private static PatientModel patientModel;

	public PatientController(JFrame frame, PatientModel model) {
		this.frame = frame;
		patientModel = model;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		PatientModel model = new PatientModel();
		PatientController control = new PatientController(frame, model);
		MenuPage menu = new MenuPage(frame);
		GotoReqInputListener r = new GotoReqInputListener();
		GotoDatabaseListener d = new GotoDatabaseListener();
		menu.addRegisterListener(r);
		menu.addViewListener(d);

	}

	// add what the model should do with the information for each actionlistener
	// call the approrpiate actionlisteners for each page it goes to
	// should check if the previous page's text is filled out
	static class GotoReqInputListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			ReqInfoPage reqInfo = new ReqInfoPage(frame);
			GotoMenuListener b = new GotoMenuListener();
			GotoOptInfoListener n = new GotoOptInfoListener(reqInfo); // maybe take previous page a parameter for action
																		// listeners to see if fields put in
			reqInfo.addBackListener(b);
			reqInfo.addNextListener(n);
		}
	}

	static class GotoMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			MenuPage menu = new MenuPage(frame);
			if (menu.isUndo()) {
				menu.hideUndo();
			}

			GotoReqInputListener r = new GotoReqInputListener();
			GotoDatabaseListener d = new GotoDatabaseListener();
			menu.addRegisterListener(r);
			menu.addViewListener(d);

		}

	}

	static class GotoOptInfoListener implements ActionListener {
		ReqInfoPage reqInfo;
		boolean isCheckReqInfo;

		public GotoOptInfoListener(ReqInfoPage reqInfo) {
			this.reqInfo = reqInfo;
			isCheckReqInfo = true;
		}

		public GotoOptInfoListener() {
			isCheckReqInfo = false;
		}

		public void actionPerformed(ActionEvent e) {

			if (isCheckReqInfo) {
				String[] info = reqInfo.getRequiredInfo();

				for (String a : info) {
					System.out.println(a);
					if (a.isEmpty()) {
						return;
						// error message
					}
				}

				// adding a new patient into the patient list
				// what if you go next next and then come back again? Maybe have it delete the
				// first patient everytime because if you dont
				// need to check if texts are empty, then you are going back. Going back means
				// you already created a new patient but now want to go back.
				Patient patient = new Patient(info[0], info[1], info[2], info[3], info[4], info[5], info[6], info[7],
						info[8], info[9], info[10], info[11], info[12], info[13], info[14]);

				patientModel.addPatient(patient);

			}

			OptInfoPage optInfo = new OptInfoPage(frame);
			GotoReqInputListener b = new GotoReqInputListener();
			GotoTinHypStatusListener n = new GotoTinHypStatusListener(optInfo);

			// adding the rest of optional information of a patient

			optInfo.addBackListener(b);
			optInfo.addNextListener(n);

		}

	}

	static class GotoTinHypStatusListener implements ActionListener {
		OptInfoPage optInfo;
		boolean isCheckOptional;

		public GotoTinHypStatusListener() {
			isCheckOptional = false;
		}

		public GotoTinHypStatusListener(OptInfoPage optInfo) {
			this.optInfo = optInfo;
			isCheckOptional = true;
		}

		public void actionPerformed(ActionEvent e) {
			if (isCheckOptional) {
				Patient patient = patientModel.getPatient(patientModel.getSize() - 1);
				patient.setOccupation(optInfo.getOccupation());
				patient.setWorkStatus(optInfo.getWorkStatus());
				patient.setEduDeg(optInfo.getEducationDegree());

			}
			TinHypInputPage tinHyp = new TinHypInputPage(frame);
			GotoOptInfoListener b = new GotoOptInfoListener();
			GotoInputMedListener n = new GotoInputMedListener(tinHyp);
			tinHyp.addBackListener(b);
			tinHyp.addNextListener(n);

		}

	}

	static class GotoInputMedListener implements ActionListener {
		private InputMedPage inputMedPage;
		private boolean isCheckMedicine;
		private TinHypInputPage page;
		private boolean isCheckStatus;

		public GotoInputMedListener() {
			isCheckMedicine = false;
			isCheckStatus = false;
		}

		public GotoInputMedListener(InputMedPage page) {
			inputMedPage = page;
			isCheckMedicine = true;
			isCheckStatus = false;
		}

		public GotoInputMedListener(TinHypInputPage page) {
			isCheckMedicine = false;
			isCheckStatus = true;
			this.page = page;
		}

		public void actionPerformed(ActionEvent e) {
			if (isCheckMedicine) {
				String[] medInfo = inputMedPage.getRequiredInfo();
				for (String a : medInfo) {
					if (a.isEmpty()) {
						// error
						return;
					}

				}
				Medicine med = new Medicine(medInfo[0], medInfo[1], medInfo[2], medInfo[3], medInfo[4], medInfo[5],
						medInfo[6], medInfo[7], medInfo[8]);
				System.out.println(medInfo[0] + medInfo[1] + medInfo[2] + medInfo[3] + medInfo[4] + medInfo[5]
						+ medInfo[6] + medInfo[7] + medInfo[8]);
				Patient patient = patientModel.getPatient(patientModel.getSize() - 1);

				patient.addMedicine(med);
				for (Medicine a : patient.getMedicine()) {
					System.out.println(a);
				}

			}
			if (isCheckStatus) {
				Patient patient = patientModel.getPatient(patientModel.getSize() - 1);
				patient.setTinAndCusDesc(page.getTinHypStatus());
				System.out.print(patient);
			}
			InputMedPage inputMedPage = new InputMedPage(frame);
			GotoTinHypStatusListener b = new GotoTinHypStatusListener();
			GotoInputMedListener m = new GotoInputMedListener(inputMedPage);
			// figure out how do to do undo button
			GotoUndoMenuListener f = new GotoUndoMenuListener();
			inputMedPage.addBackListener(b);
			inputMedPage.addNextListener(m);
			inputMedPage.addFinishListener(f);
		}

	}

	static class GotoUndoMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			MenuPage undoMenu = new MenuPage(frame, true);
			GotoReqInputListener r = new GotoReqInputListener();
			GotoDatabaseListener d = new GotoDatabaseListener();
			UndoPatientListener m = new UndoPatientListener();

			undoMenu.addRegisterListener(r);
			undoMenu.addViewListener(d);
			undoMenu.addUndoListener(m);
		}

	}

	static class UndoPatientListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			MenuPage undoMenu = new MenuPage(frame);
			patientModel.undoPatient();
			GotoReqInputListener r = new GotoReqInputListener();
			GotoDatabaseListener d = new GotoDatabaseListener();
			undoMenu.addRegisterListener(r);
			undoMenu.addViewListener(d);

		}
	}

		static class GotoInfoListener implements ActionListener {
		PatientDataPage db;
		boolean isCheckSelected;
		Patient patient;

		public GotoInfoListener(PatientDataPage db) {
			this.db = db;
			isCheckSelected = true;
		}
		public GotoInfoListener(Patient p)
		{
			patient =p; 
		}

		public void actionPerformed(ActionEvent e) {
			if (isCheckSelected == true) {
				if (db.isPatientSelected() == true) // then button will work
				{
					Patient patient = db.whichPatient();
					PatientInfoPage infoPage = new PatientInfoPage(frame, patient.getInfo());
					GotoDatabaseListener GotoData = new GotoDatabaseListener();
					GotoMedicationListener GotoMedication = new GotoMedicationListener(patient);
					infoPage.addBackListener(GotoData);
					infoPage.addNextListener(GotoMedication);

					infoPage.addUpdateListener((a) -> {
						String[] info = infoPage.getEditedInfo();
						patient.setPatientID(info[0]);
						patient.setDateAdded(info[1]);
						patient.setFirstName(info[2]);
						patient.setLastName(info[3]);
						patient.setBirthDay(info[4]);
						patient.setGender(info[5]);
						patient.setPhoneNum(info[6]);
						patient.setSocialSecureNum(info[7]);
						patient.setAddress(info[8]);
						patient.setAddress2(info[9]);
						patient.setCity(info[10]);
						patient.setState(info[11]);
						patient.setZip(info[12]);
						patient.setCountry(info[13]);
						patient.setInsuranceNum(info[14]);
						patient.setOccupation(info[15]);
						patient.setWorkStatus(info[16]);
						patient.setEduDeg(info[17]);
						patient.setTinAndCusDesc(info[18]);
					}

					// make medication listener
					// infoPage.addNextListener(medicationP);
					);
				}
			}
			else
			{
				
				PatientInfoPage infoPage = new PatientInfoPage(frame, patient.getInfo());
				GotoDatabaseListener GotoData = new GotoDatabaseListener();
				GotoMedicationListener GotoMedication = new GotoMedicationListener(patient);
				infoPage.addBackListener(GotoData);
				infoPage.addNextListener(GotoMedication);

				infoPage.addUpdateListener((a) -> {
					String[] info = infoPage.getEditedInfo();
					patient.setPatientID(info[0]);
					patient.setDateAdded(info[1]);
					patient.setFirstName(info[2]);
					patient.setLastName(info[3]);
					patient.setBirthDay(info[4]);
					patient.setGender(info[5]);
					patient.setPhoneNum(info[6]);
					patient.setSocialSecureNum(info[7]);
					patient.setAddress(info[8]);
					patient.setAddress2(info[9]);
					patient.setCity(info[10]);
					patient.setState(info[11]);
					patient.setZip(info[12]);
					patient.setCountry(info[13]);
					patient.setInsuranceNum(info[14]);
					patient.setOccupation(info[15]);
					patient.setWorkStatus(info[16]);
					patient.setEduDeg(info[17]);
					patient.setTinAndCusDesc(info[18]);
				
				});
			}
			

		}
	}

	static class GotoAddVisitListener implements ActionListener {
		PatientDataPage db;

		public GotoAddVisitListener(PatientDataPage db) {
			this.db = db;
		}

		public void actionPerformed(ActionEvent e) {

			if (db.isPatientSelected() == true) // then button will work
			{
				VisitInfoPage visitInfoPage = new VisitInfoPage(frame, db.whichPatient().getLastName());
				GotoDatabaseListener databaseListener = new GotoDatabaseListener(); 
				visitInfoPage.addBackListener(databaseListener); 
				GotoInputAudEvalListener b = new GotoInputAudEvalListener(db, visitInfoPage);
				visitInfoPage.addNextListener(b);
				// VisitInfoPage visitPage = new VisitInfoPage(frame,
				// db.whichPatient().getInfo());
			}

		}
	}
	
	static class GotoInputAudEvalListener implements ActionListener{
		private boolean isAddVisit; 
		private PatientDataPage db; 
		VisitInfoPage visitInfoPage; 
		public GotoInputAudEvalListener(PatientDataPage db, VisitInfoPage visitInfoPage)
		{
			isAddVisit = true; 
			this.db = db; 
			this.visitInfoPage = visitInfoPage; 

		}
		public GotoInputAudEvalListener(PatientDataPage db)
		{
			this.db = db; 
			isAddVisit = false; 
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (isAddVisit)
			{
				String[] visitInfo = visitInfoPage.getInfo(); 
				Visit visit = new Visit(visitInfo[0], visitInfo[1], visitInfo[2], visitInfo[3], visitInfoPage.isSoundTherapyChecked(), visitInfoPage.isEarMeasureChecked(), visitInfoPage.isCounselChecked());
				db.whichPatient().addVisit(visit); 
			}
			AudioEvalPage audioEvalPage = new AudioEvalPage(frame, db.whichPatient().getLastName());
			GotoAddVisitListener addVisitListener = new GotoAddVisitListener(db); 
			audioEvalPage.addBackListener(addVisitListener);
			GotoAssignCategoryListener categoryListener = new GotoAssignCategoryListener(db, audioEvalPage);
			audioEvalPage.addNextListener(categoryListener); 
			
		}
	}
	
	static class GotoAssignCategoryListener implements ActionListener{
		PatientDataPage db; 
		boolean isAddEval; 
		AudioEvalPage audioEvalPage; 
	
		public GotoAssignCategoryListener(PatientDataPage db, AudioEvalPage audioEvalPage)
		{
			this.db = db; 
			this.audioEvalPage = audioEvalPage;
			this.isAddEval = true;
		}
		public GotoAssignCategoryListener(PatientDataPage db)
		{
			this.db = db;
			isAddEval = false; 
			
		}
		
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (isAddEval)
			{
				String[] evalInfo = audioEvalPage.getInfo(); 
				Evaluation eval = new Evaluation(evalInfo[0],evalInfo[1],evalInfo[2],evalInfo[3],evalInfo[4],evalInfo[5],evalInfo[6],evalInfo[7],evalInfo[8],evalInfo[9]);
				db.whichPatient().getLastVisit().setEvaluation(eval);
				
				
			}
			CategoryPage catPage = new CategoryPage(frame, db.whichPatient().getLastName()); 
			GotoInputAudEvalListener b = new GotoInputAudEvalListener(db);
			GotoDatabaseListener f = new GotoDatabaseListener(catPage, db.whichPatient());
			GotoMedicationListener m  = new GotoMedicationListener(db.whichPatient());
			GotoInfoListener mh = new GotoInfoListener(db.whichPatient());
			
			catPage.addBackListener(b);
			catPage.addFinishListener(f);
			catPage.addMedicationListener(m);
			catPage.addMedHistoryListener(mh);
			
		}
	}

	static class GotoDeleteListener implements ActionListener {
		PatientDataPage db;

		public GotoDeleteListener(PatientDataPage db) {
			this.db = db;
		}

		public void actionPerformed(ActionEvent e) {

			if (db.isPatientSelected() == true) // then button will work
			{
				patientModel.removePatient(db.whichPatient()); // deletes patient

				PatientDataPage database = new PatientDataPage(frame, patientModel.patientList);
				GotoMenuListener m = new GotoMenuListener();
				GotoReqInputListener n = new GotoReqInputListener();
				GotoInfoListener i = new GotoInfoListener(database);
				GotoAddVisitListener v = new GotoAddVisitListener(database);
				GotoDeleteListener d = new GotoDeleteListener(database);
				GotoAddHistoryListener h = new GotoAddHistoryListener(database);
				GotoScheduleListener s = new GotoScheduleListener(database);

				database.addInfoListener(i);
				database.addVisitListener(v);
				database.addDeleteListener(d);
				database.addHistoryListener(h);
				database.addScheduleListener(s);

				database.addBackListener(m);
				database.addNextListener(n);
			}

		}
	}

	static class GotoAddHistoryListener implements ActionListener {
		PatientDataPage db;
		boolean isCheckSelected; 

		public GotoAddHistoryListener(PatientDataPage db) {
			this.db = db;
			isCheckSelected = true; 
		}
		public GotoAddHistoryListener(PatientDataPage db, boolean isCheckSelected)
		{
			this.db = db; 
			isCheckSelected = false; 
		}

		public void actionPerformed(ActionEvent e) {
			if (isCheckSelected == true)
			{

				if (db.isPatientSelected() == true) // then button will work
				{
					VisitHistPage historyPage = new VisitHistPage(frame, db.whichPatient().getDoneVisit(),
						db.whichPatient().getFirstName(), db.whichPatient().getLastName());

					GotoDatabaseListener d = new GotoDatabaseListener();
					GotoVisitInfoListener i = new GotoVisitInfoListener( db,  historyPage);
					GotoSpecificAudioEvalListener a = new GotoSpecificAudioEvalListener();
					GotoEditVisitHistListener ev = new GotoEditVisitHistListener();

					historyPage.addBackListener(d);
					historyPage.addEditInfoListener(ev);
					historyPage.addInfoListener(i);
					historyPage.addEvaluationListener(a);

				}
			}
			else
			{
				VisitHistPage historyPage = new VisitHistPage(frame, db.whichPatient().getDoneVisit(),
						db.whichPatient().getFirstName(), db.whichPatient().getLastName());

					GotoDatabaseListener d = new GotoDatabaseListener();
					GotoVisitInfoListener i = new GotoVisitInfoListener( db,  historyPage);
					GotoSpecificAudioEvalListener a = new GotoSpecificAudioEvalListener();
					GotoEditVisitHistListener ev = new GotoEditVisitHistListener();

					historyPage.addBackListener(d);
					historyPage.addEditInfoListener(ev);
					historyPage.addInfoListener(i);
					historyPage.addEvaluationListener(a);
				
			}
			

		}
	}

	static class GotoVisitInfoListener implements ActionListener {
		PatientDataPage db; 
		VisitHistPage historyPage; 
		public GotoVisitInfoListener(PatientDataPage db, VisitHistPage historyPage)
		{
			this.db = db;
			this.historyPage = historyPage; 
		}

		public void actionPerformed(ActionEvent e) {
			if (historyPage.isVisitSelected() == true)
			{
				Patient patient = db.whichPatient();
				Visit visit = historyPage.whichVisit(); 
				SpecificVisitInfoPage specVisInfoPage = new SpecificVisitInfoPage(frame, patient.getLastName(), visit.getSeqNum(), visit.getDate(), visit.getSeqNum(), visit.getTreatmentProgress(), 
						visit.getInterviewForm(), visit.isSoundTherapy(), visit.isRealEarMeasurement(), visit.isCounseling()); 
				GotoAddHistoryListener aDListener = new GotoAddHistoryListener(db, false); 
				specVisInfoPage.addBackListener(aDListener);
			
			}

			// add stuff

		}
	}

	static class GotoSpecificAudioEvalListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			// add stuff

		}
	}

	static class GotoEditVisitHistListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			// add stuff

		}
	}

	static class GotoScheduleListener implements ActionListener
	{
		PatientDataPage db;
		public GotoScheduleListener(PatientDataPage db)
		{
			this.db = db;
		}
		public void actionPerformed(ActionEvent e)
		{
			
			if(db.isPatientSelected() == true) //then button will work
			{
				
				db.addPopup();
				GotoEnterScheduleListener es = new GotoEnterScheduleListener(db.whichPatient());
				 JFrame popup= db.addEnterListener(es);
				 es.retrieveJFrame(popup);
				
			}
			
		}
	}
	static class GotoEnterScheduleListener implements ActionListener
	{
		Patient p;
		JFrame popup;
		public GotoEnterScheduleListener(Patient p)
		{
			this.p = p;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			//add schedule to patient in model
			
			//closes popup
			popup.dispose();
			
			//takes u back to PatientDataPage
			PatientDataPage database = new PatientDataPage(frame, patientModel.patientList);
			GotoMenuListener m = new GotoMenuListener();
			GotoReqInputListener n = new GotoReqInputListener();
			GotoInfoListener i = new GotoInfoListener(database);
			GotoAddVisitListener v = new GotoAddVisitListener(database);
			GotoDeleteListener d = new GotoDeleteListener(database);
			GotoAddHistoryListener h = new GotoAddHistoryListener(database);
			GotoScheduleListener s = new GotoScheduleListener(database);

			 

			database.addInfoListener(i);
			database.addVisitListener(v);
			database.addDeleteListener(d);
			database.addHistoryListener(h);
			database.addScheduleListener(s);

			database.addBackListener(m);
			database.addNextListener(n);
			System.out.println(patientModel);
			
		}
		public void retrieveJFrame(JFrame popup)
		{
			this.popup = popup;
		}
	}

	static class GotoDatabaseListener implements ActionListener {
		private boolean isAssignCat; 
		private CategoryPage catPage; 
		private Patient patient; 
		public GotoDatabaseListener()
		{
			
		}
		/**
		 * constructor for coming back from assigning a category
		 * @param catPage
		 */
		public GotoDatabaseListener(CategoryPage catPage, Patient patient)
		{
			isAssignCat = true; 
			this.catPage = catPage; 
			this.patient = patient; 
		}
		public void actionPerformed(ActionEvent e) {
			if (isAssignCat)
			{
				//information of catPage and add it to the last visit of the patient. 
			}
			PatientDataPage database = new PatientDataPage(frame, patientModel.patientList);
			GotoMenuListener m = new GotoMenuListener();
			GotoReqInputListener n = new GotoReqInputListener();
			GotoInfoListener i = new GotoInfoListener(database);
			GotoAddVisitListener v = new GotoAddVisitListener(database);
			GotoDeleteListener d = new GotoDeleteListener(database);
			GotoAddHistoryListener h = new GotoAddHistoryListener(database);
			GotoScheduleListener s = new GotoScheduleListener(database);

			database.addInfoListener(i);
			database.addVisitListener(v);
			database.addDeleteListener(d);
			database.addHistoryListener(h);
			database.addScheduleListener(s);

			database.addBackListener(m);
			database.addNextListener(n);
		}

	}

	static class GotoMedicationListener implements ActionListener {
		Patient p;

		public GotoMedicationListener(Patient p) {
			this.p = p;
		}

		public void actionPerformed(ActionEvent e) {
			Object[] medList = p.getMedicine().toArray();

			String[] medNameList = new String[medList.length];
			for (int i = 0; i < medList.length; i++) {
				medNameList[i] = ((Medicine) medList[i]).getMedicineName();
			}

			PatientMedListPage medListPage = new PatientMedListPage(frame, p.getLastName(), medNameList);
			ArrayList<JButton> medButtons = medListPage.getMedButtons();

			for (int i = 0; i < medButtons.size(); i++) {
				JButton medButton = medButtons.get(i);
				GotoSpecificMedListener specMedListener = new GotoSpecificMedListener(p,
						p.getMedicine(medButton.getText()));
				medButton.addActionListener(specMedListener);

			}
			GotoInfoListener infoListener = new GotoInfoListener(p);
			medListPage.addBackListener(infoListener);

		}
	}

	static class GotoSpecificMedListener implements ActionListener {

		Patient p;
		Medicine m;

		public GotoSpecificMedListener(Patient p, Medicine m) {
			this.p = p;
			this.m = m;
		}

		public void actionPerformed(ActionEvent e) {
			SpecificMedPage specMedPage = new SpecificMedPage(frame, m.getInfo());
			GotoMedicationListener medListListener = new GotoMedicationListener(p);

			specMedPage.addBackListener(medListListener);
			specMedPage.addUpdateListener((s) -> {
				String[] medInfo = specMedPage.getEditedInfo();
				m.setMedicineName(medInfo[0]);
				m.setMedicantName(medInfo[1]);
				m.setDoseSize(medInfo[2]);
				m.setDuration(medInfo[3]);
				m.setChemCategoryName(medInfo[4]);
				m.setAction(medInfo[5]);
				m.setApplication(medInfo[6]);
				m.setUsualDose(medInfo[7]);
				m.setSideEffect(medInfo[8]);

			});

		}

	}

}
