<h1>Character view: <?php echo $charinfo->getName(); ?></h1>
<table>
  <thead>
    <tr>
      <th>Class</th>
      <th>EP</th>
      <th>GP</th>
      <th>Priority</th>
      <th>Member since</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><?php echo $charinfo->getClasses()->getName() ?></td>
      <td><?php echo $charinfo->getEp(); ?></td>
      <td><?php echo $charinfo->getGp(); ?></td>
      <td><?php echo $charinfo->getPriority(); ?></td>
      <td><?php echo date('m/d/y', strtotime($charinfo->getJoinedOn())); ?></td>
    </tr>
  </tbody>
</table>

<h3>Raid Attendance</h3>
<table>
  <thead>
    <tr>
      <td>Date</td>
      <td>Boss</td>
      <td>Zone</td>
      <td>Note</td>
      <td>Base value</td>
      <td>Inflated value</td>
    </tr>
  </thead>
  <tbody>
    <?php foreach($attendanceRecords as $record): ?>
    <tr>
      <?php $raid = $record->getRaids(); ?>
      <td><?php $raid->echoMdyLink(); ?></td>
      <td><?php $raid->getBoss()->echoLink(); ?></td>
      <td><?php $raid->getZone()->echoLink(); ?></td>
      <td><?php $raid->echoNoteLink(); ?></td>
      <td><?php echo $raid->getBaseVal(); ?></td>
      <td><?php echo $raid->getInflatedVal(); ?></td>
    </tr>
    <?php endforeach; ?>
  </tbody>
</table>

<h3>Item Purchase History</h3>
<table>
  <thead>
    <tr>
      <td>Date</td>
      <td>Item Name</td>
      <td>Boss</td>
      <td>Zone</td>
      <td>Base value</td>
      <td>Inflated value</td>
    </tr>
  </thead>
  <tbody>
    <?php foreach($items as $item): ?>
    <tr>
      <td><?php $item->getRaids()->echoMdyLink(); ?></td>
      <td><?php $item->echoLink(); ?></td>
      <td><?php $item->getBoss()->echoLink(); ?></td>
      <td><?php $item->getZone()->echoLink(); ?></td>
      <td><?php echo $item->getBaseVal(); ?></td>
      <td><?php echo $item->getInflatedVal(); ?></td>
    </tr>
    <?php endforeach; ?>
  </tbody>
</table>