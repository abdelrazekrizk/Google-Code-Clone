<h2>Raid view</h2>

<h3>Raid info</h3>
<table>
  <thead>
    <tr>
      <th>Date</th>
      <th>Boss</th>
      <th>Zone</th>
      <th>Note</th>
      <th>Base</th>
      <th>Inflated</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><?php $raidinfo->echoMdyLink() ?></td>
      <td><?php $raidinfo->getBoss()->echoLink() ?></td>
      <td><?php $raidinfo->getZone()->echoLink() ?></td>
      <td><?php echo $raidinfo->getNote() ?></td>
      <td><?php echo $raidinfo->getBaseVal(); ?></td>
      <td><?php echo $raidinfo->getInflatedVal(); ?></td>
    </tr>
  </tbody>
</table>

<h3>Attendees</h3>
<table>
  <thead>
    <tr>
      <th>Name</th>
    </tr>
  </thead>
  <tbody>
  <?php foreach($attendees as $attendee): ?>
    <tr>
      <td><?php $attendee->echoLink(); ?></td>
    </tr>
  <?php endforeach; ?>
  </tbody>
</table>

<h3>Items awarded</h3>
<table>
  <thead>
    <tr>
      <th>Name</th>
      <th>Awarded to</th>
      <th>Base</th>
      <th>Inflated</th>
    </tr>
  </thead>
  <tbody>
  <?php foreach($items as $item): ?>
    <tr>
      <td><?php $item->echoLink(); ?></td>
      <td><?php $item->getAwardee()->echoLink(); ?></td>
      <td><?php echo $item->getBaseVal(); ?></td>
      <td><?php echo $item->getInflatedVal(); ?></td>
    </tr>
  <?php endforeach; ?>
  </tbody>
</table>