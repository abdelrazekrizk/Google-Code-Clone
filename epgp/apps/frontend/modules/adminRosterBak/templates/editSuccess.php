<h1>Edit AdminRoster</h1>

<form action="<?php echo url_for('adminRoster/edit')?>" method="post">
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
          <input type="text" name="name" id="name" value="<?php echo $char->getName(); ?>" />
        </td>
      </tr>
      <tr>
        <th>Class</th>
        <td>
          <select name="class" id="class">
<?php foreach($classes as $class): ?>
<?php $id = $class->getId(); ?>
<?php if($id == $char->getCharclass()): ?>
            <option value="<?php echo $class->getId() ?>" selected="selected"><?php echo $class ?></option>
<?php else: ?>
            <option value="<?php echo $class->getId() ?>"><?php echo $class ?></option>
<?php endif; ?>
<?php endforeach; ?>
          </select>
        </td>
      </tr>
      <tr>
        <th>Race</th>
        <td>
          <select name="race" id="race">
<?php foreach($races as $race): ?>
<?php $id = $race->getId(); ?>
<?php if($id == $char->getCharrace()): ?>
            <option value="<?php echo $race->getId() ?>" selected="selected"><?php echo $race ?></option>
<?php else: ?>
            <option value="<?php echo $race->getId() ?>"><?php echo $race ?></option>
<?php endif; ?>
<?php endforeach; ?>
          </select>
        </td>
      </tr>
      <tr>
        <th>Joined On</th>
        <td>
<?php include_partial('admin/timestampform', array('name' => 'joinedon', 'date' => strtotime($char->getJoinedOn()) )) ?>
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