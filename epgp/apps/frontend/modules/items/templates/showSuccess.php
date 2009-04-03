<h1>Item view: <?php echo $itemname; ?></h1>

<h2>Awardees</h2>
<table>
  <thead>
    <tr>
      <th>Date</th>
      <th>Raid Note</th>
      <th>Player</th>
      <th>Base</th>
      <th>Inflated</th>
    </tr>
  </thead>
  <tbody>
  <?php foreach($awards as $award): ?>
    <tr>
      <td><?php $award->getRaid()->echoMdyLink(); ?></td>
      <td><?php $award->getRaid()->echoNoteLink(); ?></td>
      <td><?php $award->getAwardee()->echoLink(); ?></td>
      <td><?php echo $award->getBaseVal(); ?></td>
      <td><?php echo $award->getInflatedVal(); ?></td>
    </tr>
  <?php endforeach; ?>
  </tbody>
</table>
