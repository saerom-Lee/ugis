<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- ========== Css Files ========== -->
<link href="/css/root.css" rel="stylesheet">
<link href="css/all.css" rel="stylesheet">
<script>
function selectAll(selectAll)  {
  const checkboxes 
       = document.getElementsByName('animal');
  
  checkboxes.forEach((checkbox) => {
    checkbox.checked = selectAll.checked;
  })
}
</script>
<!-- ================================================
jQuery Library
================================================ -->
<script src="/js/jquery.min.js"></script>

<!-- ================================================
Bootstrap Core JavaScript File
================================================ -->
<script src="/js/bootstrap/bootstrap.min.js"></script>

<!-- ================================================
Plugin.js - Some Specific JS codes for Plugin Settings
================================================ -->
<script src="/js/plugins.js"></script>

<!-- ================================================
Summernote
================================================ -->
<script src="/js/summernote/summernote.min.js"></script>

<!-- ================================================
Data Tables
================================================ -->
<script src="/js/datatables/datatables.min.js"></script>

<!-- ================================================
Sweet Alert
================================================ -->
<script src="/js/sweet-alert/sweet-alert.min.js"></script>

<!-- ================================================
Kode Alert
================================================ -->
<script src="/js/kode-alert/main.js"></script>

<!-- ================================================
jQuery UI
================================================ -->
<script src="/js/jquery-ui/jquery-ui.min.js"></script>

<!-- ================================================
Moment.js
================================================ -->
<script src="/js/moment/moment.min.js"></script>

<!-- ================================================
Full Calendar
================================================ -->
<script src="/js/full-calendar/fullcalendar.js"></script>

<!-- ================================================
Bootstrap Date Range Picker
================================================ -->
<script src="/js/date-range-picker/daterangepicker.js"></script>

<script src="/js/map/v6.7.0/ol.js"></script>
<link rel="stylesheet" href="/js/map/v6.7.0/ol.css">
<%--<script src="https://cdnjs.cloudflare.com/ajax/libs/proj4js/1.1.0/proj4js-combined.min.js"></script>--%>
<script type="text/javascript" src="js/map/proj4js-combined.js"></script>
<script src="/js/map/map.js"></script>
<script src="/js/map/gis.js"></script>
<script src="js/map/common.js"></script>

