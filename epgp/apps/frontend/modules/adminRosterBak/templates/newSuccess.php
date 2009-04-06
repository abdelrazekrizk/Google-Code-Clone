<h1>New AdminRoster</h1>

<form action="<?php echo url_for('adminRoster/create')?>" method="post">
<input type="hidden" name="sf_method" value="put" />
  <table>
    <tfoot>
      <tr>
        <td colspan="2">
          <a href="<?php echo url_for('adminRoster/index') ?>">Cancel</a>
          <input type="submit" value="Save" />
        </td>
      </tr>
    </tfoot>
    <tbody>
      <tr>
        <th>Name</th>
        <td>
          <input type="text" name="name" id="name" />
        </td>
      </tr>
      <tr>
        <th>Class</th>
        <td>
          <select name="class" id="class">
<?php foreach($classes as $class): ?>
            <option value="<?php echo $class->getId() ?>"><?php echo $class ?></option>
<?php endforeach; ?>
          </select>
        </td>
      </tr>
      <tr>
        <th>Race</th>
        <td>
          <select name="race" id="race">
<?php foreach($races as $race): ?>
            <option value="<?php echo $race->getId() ?>"><?php echo $race ?></option>
<?php endforeach; ?>
          </select>
        </td>
      </tr>
      <tr>
        <th>Joined On</th>
        <td>
<?php include_partial('admin/timestampform', array('name' => 'joinedon', 'date' => strtotime('today') )) ?>
        </td>
      </tr>
      <tr>
        <th>Active?</th>
        <td>
          <input type="checkbox" name="active" value="active" />
        </td>
      </tr>
    </tbody>
  </table>
</form>
