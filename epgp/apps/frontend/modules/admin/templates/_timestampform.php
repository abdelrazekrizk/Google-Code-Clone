<?php 
if(!isset($date)){
  $date = strtotime('today');
}
$thismonth = intval(date('n', $date));
$thisday = intval(date('j', $date));
$thisyear = intval(date('Y', $date));

$arr = array(
  array("Month", 1, 12, $thismonth),
  array("Day", 1, 31, $thisday),
  array("Year", $thisyear - 4, $thisyear + 4, $thisyear),
);
?>

<?php for($j = 0; $j < count($arr); $j++): ?>
<?php list($label, $min, $max, $default) = $arr[$j]; ?>
    <select name="<?php echo $name.$label ?>" id="<?php echo $name.$label ?>">
<?php for($i = $min; $i <= $max; $i++): ?>
<?php if($i == $default): ?>
      <option value="<?php echo $i ?>" selected="selected"><?php echo $i ?></option>
<?php else: ?>
      <option value="<?php echo $i ?>"><?php echo $i ?></option>
<?php endif; ?>
<?php endfor; ?>
    </select>
<?php if($j != count($arr) - 1): ?>
/
<?php endif; ?>
<?php endfor; ?>