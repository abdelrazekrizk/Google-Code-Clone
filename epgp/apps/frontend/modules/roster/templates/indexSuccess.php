<h1>Roster view</h1>

<table>
  <thead>
    <tr>
      <th>Name</th>
      <th>Class</th>
      <th>EP</th>
      <th>GP</th>
      <th>Priority</th>
    </tr>
  </thead>
  <tbody>
  <?php foreach($characters as $character): ?>
    <tr>
      <td><?php $character->echoLink() ?></td>
      <td><?php echo $character->getClass(); ?></td>
      <td><?php echo $character->getEp() ?></td>
      <td><?php echo $character->getGp() ?></td>
      <td><?php echo $character->getPriority() ?></td>
    </tr>
  <?php endforeach; ?>
  </tbody>
</table>

